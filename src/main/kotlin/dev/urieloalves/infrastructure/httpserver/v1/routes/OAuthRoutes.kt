package dev.urieloalves.infrastructure.httpserver.v1.routes

import io.ktor.server.routing.Route

fun Route.oAuthRoutes() {

//    val oAuthService = OAuthServiceImpl(
//        userDao = UserDaoImpl(),
//        discordService = DiscordServiceImpl(
//            discordClient = DiscordClientImpl()
//        ),
//        jwtService = JwtServiceImpl()
//    )
//
//    route("/oauth") {
//
//        get("/discord") {
//            call.respondRedirect(Env.DISCORD_REDIRECT_URL, permanent = true)
//        }
//
//        get("/discord/callback") {
//            val code = call.request.queryParameters["code"]
//                ?: throw ServerException("Could not obtain query parameter 'code' from discord")
//            call.respond(
//                oAuthService.handleDiscordOAuthCallback(code)
//            )
//        }
//    }
}