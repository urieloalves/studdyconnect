package dev.urieloalves.routes.v1

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.urieloalves.clients.DiscordClient
import dev.urieloalves.configs.Env
import dev.urieloalves.data.dao.DiscordUserDaoImpl
import dev.urieloalves.routes.v1.responses.TokenResponse
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.coroutines.runBlocking
import java.time.Instant

fun Route.oAuthRoutes() {

    route("/oauth") {

        get("/discord") {
            call.respondRedirect(Env.DISCORD_REDIRECT_URL, permanent = true)
        }

        get("/discord/callback") {
            val code = call.parameters["code"] ?: throw Error("Could not obtain code from discord")

            runBlocking {
                val discordToken = DiscordClient.getAccessToken(code)

                val userFromDiscord = DiscordClient.getUser(discordToken)

                val discordUserDao = DiscordUserDaoImpl()

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

                call.respond(
                    TokenResponse(
                        token = token
                    )
                )
            }
        }
    }
}