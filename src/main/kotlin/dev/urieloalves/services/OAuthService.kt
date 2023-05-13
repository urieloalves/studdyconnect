package dev.urieloalves.services

import dev.urieloalves.clients.DiscordClient
import dev.urieloalves.data.dao.UserDao
import dev.urieloalves.routes.v1.responses.OAuthResponse
import dev.urieloalves.routes.v1.responses.UserResponse

class OAuthService(
    val userDao: UserDao,
    val discordService: DiscordService,
    val discordClient: DiscordClient,
    val jwtService: JwtService
) {

    suspend fun handleDiscordOAuthCallback(code: String): OAuthResponse {
        val discordToken = discordClient.getAccessToken(code)

        val discordUser = discordClient.getUser(discordToken)

        var existentUser = userDao.getByDiscordId(discordUser.id)

        if (existentUser == null) {
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
    }
}