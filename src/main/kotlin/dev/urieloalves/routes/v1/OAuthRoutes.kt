package dev.urieloalves.routes.v1

import dev.urieloalves.clients.DiscordClientImpl
import dev.urieloalves.configs.Env
import dev.urieloalves.data.dao.UserDaoImpl
import dev.urieloalves.data.models.errors.ServerException
import dev.urieloalves.services.DiscordServiceImpl
import dev.urieloalves.services.JwtServiceImpl
import dev.urieloalves.services.OAuthServiceImpl
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.oAuthRoutes() {

    val oAuthService = OAuthServiceImpl(
        userDao = UserDaoImpl(),
        discordService = DiscordServiceImpl(
            discordClient = DiscordClientImpl()
        ),
        jwtService = JwtServiceImpl()
    )

    route("/oauth") {

        get("/discord") {
            call.respondRedirect(Env.DISCORD_REDIRECT_URL, permanent = true)
        }

        get("/discord/callback") {
            val code = call.request.queryParameters["code"]
                ?: throw ServerException("Could not obtain query parameter 'code' from discord")
            call.respond(
                oAuthService.handleDiscordOAuthCallback(code)
            )
        }
    }
}