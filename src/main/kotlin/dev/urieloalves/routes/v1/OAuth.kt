package dev.urieloalves.routes.v1

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

fun Route.oAuthRoutes() {

    route("/oauth") {

        get("/discord") {
            call.respondRedirect(Env.DISCORD_REDIRECT_URL, permanent = true)
        }

        get("/discord/callback") {
            val code = call.parameters["code"] ?: throw Error("Could not obtain code from discord")
            runBlocking {
                val accessToken = DiscordClient.getAccessToken(code)
                val userInfo = DiscordClient.getUser(accessToken)
                // TODO if user does not exist, create user and generate/send jwt token
                // otherwise, generate/send jwt token
                call.respond(TokenResponse("jwt-token"))
            }
        }
    }
}