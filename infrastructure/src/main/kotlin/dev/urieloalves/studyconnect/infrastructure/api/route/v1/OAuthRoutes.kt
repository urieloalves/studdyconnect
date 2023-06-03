package dev.urieloalves.studyconnect.infrastructure.api.route.v1

import dev.urieloalves.application.user.dto.InputHandleOauthUseCaseDto
import dev.urieloalves.studyconnect.infrastructure.api.error.ServerException
import dev.urieloalves.studyconnect.infrastructure.api.route.v1.response.OAuthResponse
import dev.urieloalves.studyconnect.infrastructure.api.route.v1.response.UserResponse
import dev.urieloalves.studyconnect.infrastructure.module.Module
import dev.urieloalves.studyconnect.infrastructure.shared.Env
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.oAuthRoutes() {

    route("/oauth") {

        get("/discord") {
            call.respondRedirect(
                dev.urieloalves.studyconnect.infrastructure.shared.Env.DISCORD_REDIRECT_URL,
                permanent = true
            )
        }

        get("/discord/callback") {
            val code = call.request.queryParameters["code"]
                ?: throw ServerException("Could not obtain query parameter 'code' from discord")
            val output = dev.urieloalves.studyconnect.infrastructure.module.Module.handleOauthUseCase.execute(
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