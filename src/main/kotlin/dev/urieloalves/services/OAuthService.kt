package dev.urieloalves.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.urieloalves.clients.DiscordClient
import dev.urieloalves.configs.Env
import dev.urieloalves.data.dao.DiscordUserDao
import dev.urieloalves.routes.v1.responses.TokenResponse
import java.time.Instant

class OAuthService(
    val discordUserDao: DiscordUserDao
) {

    suspend fun handleDiscordOAuthCallback(code: String): TokenResponse {
        val discordToken = DiscordClient.getAccessToken(code)

        val userFromDiscord = DiscordClient.getUser(discordToken)

        val createdDiscordUser = discordUserDao.getById(userFromDiscord.id)

        if (createdDiscordUser == null) {
            discordUserDao.create(
                id = userFromDiscord.id,
                username = userFromDiscord.username,
                email = userFromDiscord.email
            )
        }

        val token = JWT.create()
            .withClaim("id", userFromDiscord.id)
            .withExpiresAt(Instant.now().plusSeconds(Env.JWT_EXPIRES_IN_MINUTES * 60))
            .sign(Algorithm.HMAC256(Env.JWT_SECRET))

        return TokenResponse(
            token = token
        )
    }
}