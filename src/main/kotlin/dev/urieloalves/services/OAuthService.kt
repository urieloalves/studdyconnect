package dev.urieloalves.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.urieloalves.clients.DiscordClient
import dev.urieloalves.configs.Env
import dev.urieloalves.data.dao.UserDao
import dev.urieloalves.routes.v1.responses.OAuthResponse
import dev.urieloalves.routes.v1.responses.UserResponse
import java.time.Instant

class OAuthService(
    val userDao: UserDao,
    val discordService: DiscordService
) {

    suspend fun handleDiscordOAuthCallback(code: String): OAuthResponse {
        val discordToken = DiscordClient.getAccessToken(code)

        val discordUser = DiscordClient.getUser(discordToken)

        val createdDiscordUser = userDao.getById(discordUser.id)

        if (createdDiscordUser == null) {
            userDao.create(
                id = discordUser.id,
                username = discordUser.username,
                email = discordUser.email
            )

//            DiscordClient.addToServer(
//                userId = discordUser.id,
//                accessToken = discordToken
//            )
        }

//        DiscordClient.addUserToServer(discordUser.id, discordToken)

        discordService.joinServer(userId = discordUser.id, token = discordToken)

        val token = JWT.create()
            .withClaim("id", discordUser.id)
            .withExpiresAt(Instant.now().plusSeconds(Env.JWT_EXPIRES_IN_MINUTES * 60))
            .sign(Algorithm.HMAC256(Env.JWT_SECRET))

        return OAuthResponse(
            token = token,
            user = UserResponse(
                id = discordUser.id,
                username = discordUser.username,
                email = discordUser.email
            )
        )
    }
}