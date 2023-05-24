package dev.urieloalves.infrastructure.httpserver.route.v1

import dev.urieloalves.infrastructure.httpserver.route.v1.response.OAuthResponse
import dev.urieloalves.infrastructure.httpserver.route.v1.response.UserResponse
import dev.urieloalves.infrastructure.module.Module
import dev.urieloalves.infrastructure.shared.Env
import dev.urieloalves.infrastructure.shared.errors.ServerException
import dev.urieloalves.usecase.user.dto.InputHandleOauthUseCaseDto
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.oAuthRoutes() {

    route("/oauth") {

        get("/discord") {
            call.respondRedirect(Env.DISCORD_REDIRECT_URL, permanent = true)
        }

        get("/discord/callback") {
            val code = call.request.queryParameters["code"]
                ?: throw ServerException("Could not obtain query parameter 'code' from discord")
            val output = Module.handleOauthUseCase.execute(
                InputHandleOauthUseCaseDto(code)
            )
            call.respond(
                OAuthResponse(
                    token = output.token,
                    user = UserResponse(
                        id = output.userId.toString(),
                        username = output.username,
                        email = output.email
                    )
                )
            )
        }
    }
}