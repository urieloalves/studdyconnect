package dev.urieloalves.services

import dev.urieloalves.data.dao.UserDao
import dev.urieloalves.data.models.errors.CustomException
import dev.urieloalves.data.models.errors.ServerException
import dev.urieloalves.routes.v1.responses.OAuthResponse
import dev.urieloalves.routes.v1.responses.UserResponse
import io.ktor.server.plugins.NotFoundException
import org.slf4j.LoggerFactory

interface OAuthService {
    suspend fun handleDiscordOAuthCallback(code: String): OAuthResponse
}

class OAuthServiceImpl(
    val userDao: UserDao,
    val discordService: DiscordService,
    val jwtService: JwtService
) : OAuthService {

    private val logger = LoggerFactory.getLogger("OAuthServiceImpl")

    override suspend fun handleDiscordOAuthCallback(code: String): OAuthResponse {
        try {
            val discordToken = discordService.getAccessToken(code)
            logger.info("Discord authentication token was retrieved successfully")

            val discordUser = discordService.getUser(discordToken)
            logger.info("Discord user information was retrieved successfully")

            var existentUser = userDao.getByDiscordId(discordUser.id)

            if (existentUser != null) {
                logger.info("User '${existentUser.id}' is already registered - a JWT token will be generated")
            } else {
                logger.info("Discord user '${discordUser.id}' with email '$${discordUser.email}' is not registered. C")
                userDao.create(
                    discordId = discordUser.id,
                    username = discordUser.username,
                    email = discordUser.email
                )
                logger.info("User was created for discord user '${discordUser.id}'")
                existentUser = userDao.getByDiscordId(discordUser.id) ?: throw NotFoundException("Could not")
                discordService.joinServer(discordId = discordUser.id, token = discordToken)
            }

            val token = jwtService.generateToken(existentUser.id)
            logger.info("JWT token was successfully generated for user '${existentUser.id}'")

            return OAuthResponse(
                token = token,
                user = UserResponse(
                    id = existentUser.id,
                    username = existentUser.username,
                    email = existentUser.email
                )
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