package dev.urieloalves.routes.v1

import dev.urieloalves.clients.DiscordClientImpl
import dev.urieloalves.configs.Env
import dev.urieloalves.data.dao.UserDaoImpl
import dev.urieloalves.services.DiscordService
import dev.urieloalves.services.JwtServiceImpl
import dev.urieloalves.services.OAuthService
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.oAuthRoutes() {

    val oAuthService = OAuthService(
        userDao = UserDaoImpl(),
        discordService = DiscordService(),
        discordClient = DiscordClientImpl(),
        jwtService = JwtServiceImpl()
    )

    route("/oauth") {

        get("/discord") {
            call.respondRedirect(Env.DISCORD_REDIRECT_URL, permanent = true)
        }

        get("/discord/callback") {
            val code = call.request.queryParameters["code"] ?: throw Error("Could not obtain code from discord")
            call.respond(
                oAuthService.handleDiscordOAuthCallback(code)
            )
        }
    }
}