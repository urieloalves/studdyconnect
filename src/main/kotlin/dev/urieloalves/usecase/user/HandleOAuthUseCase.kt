package dev.urieloalves.usecase.user

import dev.urieloalves.domain.user.factory.UserFactory
import dev.urieloalves.domain.user.repository.UserRepository
import dev.urieloalves.infrastructure.discord.DiscordClient
import dev.urieloalves.infrastructure.discord.dto.InputGetAccessTokenDto
import dev.urieloalves.infrastructure.discord.dto.InputGetUserDto
import dev.urieloalves.infrastructure.discord.dto.InputJoinServerDto
import dev.urieloalves.infrastructure.shared.errors.CustomException
import dev.urieloalves.infrastructure.shared.errors.ServerException
import dev.urieloalves.usecase.user.dto.InputGenerateTokenUseCaseDto
import dev.urieloalves.usecase.user.dto.InputHandleOauthUseCaseDto
import dev.urieloalves.usecase.user.dto.OutputHandleOauthUseCaseDto
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
            val getAccessTokenOutput = discordClient.getAccessToken(
                InputGetAccessTokenDto(
                    code = input.code
                )
            )
            logger.info("Discord authentication token was retrieved successfully")

            val getUserOutput = discordClient.getUser(
                InputGetUserDto(
                    token = getAccessTokenOutput.accessToken
                )
            )
            logger.info("Discord user information was retrieved successfully")

            var existentUser = userRepository.findByDiscordId(getUserOutput.id)

            if (existentUser != null) {
                logger.info("User '${existentUser.id}' is already registered - a JWT token will be generated")
            } else {
                logger.info("Discord user '${getUserOutput.id}' with email '$${getUserOutput.email}' is not registered. C")
                val user = UserFactory.create(
                    email = getUserOutput.email,
                    discordId = getUserOutput.id,
                    discordUsername = getUserOutput.username
                )
                userRepository.create(user)
                logger.info("User was created for discord user '${getUserOutput.id}'")
                existentUser = userRepository.findByDiscordId(getUserOutput.id) ?: throw NotFoundException("Could not")
                discordClient.joinServer(
                    InputJoinServerDto(
                        discordUserId = getUserOutput.id,
                        token = getAccessTokenOutput.accessToken
                    )
                )
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