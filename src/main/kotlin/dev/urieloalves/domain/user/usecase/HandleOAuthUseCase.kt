package dev.urieloalves.domain.user.usecase

import dev.urieloalves.clients.DiscordClient
import dev.urieloalves.data.models.errors.CustomException
import dev.urieloalves.data.models.errors.ServerException
import dev.urieloalves.domain.user.factory.UserFactory
import dev.urieloalves.domain.user.repository.UserRepository
import dev.urieloalves.domain.user.usecase.dto.InputGenerateTokenUseCaseDto
import dev.urieloalves.domain.user.usecase.dto.InputHandleOauthUseCaseDto
import dev.urieloalves.domain.user.usecase.dto.OutputHandleOauthUseCaseDto
import io.ktor.server.plugins.NotFoundException
import org.slf4j.LoggerFactory

class HandleOAuthUseCase(
    private val discordClient: DiscordClient,
    private val userRepository: UserRepository,
    private val generateTokenUseCase: GenerateTokenUseCase
) {

    private val logger = LoggerFactory.getLogger("HandleOAuthUseCase")

    suspend fun execute(input: InputHandleOauthUseCaseDto): OutputHandleOauthUseCaseDto {
        try {
            val discordToken = discordClient.getAccessToken(input.code)
            logger.info("Discord authentication token was retrieved successfully")

            val discordUser = discordClient.getUser(discordToken)
            logger.info("Discord user information was retrieved successfully")

            var existentUser = userRepository.findByDiscordId(discordUser.id)

            if (existentUser != null) {
                logger.info("User '${existentUser.id}' is already registered - a JWT token will be generated")
            } else {
                logger.info("Discord user '${discordUser.id}' with email '$${discordUser.email}' is not registered. C")
                val user = UserFactory.create(
                    email = discordUser.email,
                    discordId = discordUser.id,
                    discordUsername = discordUser.username
                )
                userRepository.create(user)
                logger.info("User was created for discord user '${discordUser.id}'")
                existentUser = userRepository.findByDiscordId(discordUser.id) ?: throw NotFoundException("Could not")
                discordClient.joinServer(discordId = discordUser.id, token = discordToken)
            }

            val tokenOutput = generateTokenUseCase.execute(
                InputGenerateTokenUseCaseDto(existentUser.id)
            )
            logger.info("JWT token was successfully generated for user '${existentUser.id}'")

            return OutputHandleOauthUseCaseDto(
                userId = existentUser.id,
                username = existentUser.discordUser.username,
                email = existentUser.email,
                token = tokenOutput.token
            )
        } catch (e: Exception) {
            val msg = "An error occurred when handling discord oauth callback"
            logger.error(msg, e)
            when {
                e is CustomException -> throw e
                else -> throw ServerException(msg, e)
            }
        }
    }
}