package dev.urieloalves.routes.v1

import dev.urieloalves.configs.Env
import dev.urieloalves.data.dao.DiscordUserDaoImpl
import dev.urieloalves.services.OAuthService
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.coroutines.runBlocking

fun Route.oAuthRoutes() {

    val oAuthService = OAuthService(
        discordUserDao = DiscordUserDaoImpl()
    )

    route("/oauth") {

        get("/discord") {
            call.respondRedirect(Env.DISCORD_REDIRECT_URL, permanent = true)
        }

        get("/discord/callback") {
            val code = call.parameters["code"] ?: throw Error("Could not obtain code from discord")

            runBlocking {
                call.respond(
                    oAuthService.handleDiscordOAuthCallback(code)
                )
            }
        }
    }
}