package dev.urieloalves.routes.v1

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.urieloalves.clients.DiscordClient
import dev.urieloalves.configs.Env
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

                val user = DiscordClient.getUser(discordToken)

                val userExists = true
                if (userExists) {
                    val token = JWT.create()
                        .withClaim("id", user.id)
                        .withExpiresAt(Instant.now().plusSeconds(Env.JWT_EXPIRES_IN_MINUTES * 60))
                        .sign(Algorithm.HMAC256(Env.JWT_SECRET))

                    call.respond(
                        TokenResponse(
                            token = token
                        )
                    )
                } else {
                    // TODO create user
                    val token = JWT.create()
                        .withClaim("id", user.id)
                        .withExpiresAt(Instant.now().plusSeconds(Env.JWT_EXPIRES_IN_MINUTES))
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
}