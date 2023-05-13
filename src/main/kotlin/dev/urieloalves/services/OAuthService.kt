package dev.urieloalves.services

import dev.urieloalves.data.dao.UserDao
import dev.urieloalves.routes.v1.responses.OAuthResponse
import dev.urieloalves.routes.v1.responses.UserResponse
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

            val discordUser = discordService.getUser(discordToken)

            var existentUser = userDao.getByDiscordId(discordUser.id)

            if (existentUser == null) {
                logger.info("Discord user '${discordUser.id}' with email '$${discordUser.email}' is not registered. C")
                userDao.create(
                    discordId = discordUser.id,
                    username = discordUser.username,
                    email = discordUser.email
                )
                existentUser = userDao.getByDiscordId(discordUser.id)
                discordService.joinServer(discordId = discordUser.id, token = discordToken)
            }

            val token = jwtService.generateToken(existentUser!!.id)

            return OAuthResponse(
                token = token,
                user = UserResponse(
                    id = existentUser.id,
                    username = existentUser.username,
                    email = existentUser.email
                )
            )
        } catch (e: Exception) {
            logger.info("An error occurred when handling discord oauth callback", e)
            throw e
        }
    }
}