package dev.urieloalves.studyconnect.infrastructure.api

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.urieloalves.domain.shared.error.ValidationException
import dev.urieloalves.studyconnect.infrastructure.api.error.ClientException
import dev.urieloalves.studyconnect.infrastructure.api.error.CustomException
import dev.urieloalves.studyconnect.infrastructure.api.route.v1.groupRoutes
import dev.urieloalves.studyconnect.infrastructure.api.route.v1.oAuthRoutes
import dev.urieloalves.studyconnect.infrastructure.api.route.v1.response.ErrorResponse
import dev.urieloalves.studyconnect.infrastructure.shared.Env
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.principal
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.respond
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import java.util.UUID

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(CORS) {
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is CustomException -> call.respond(
                    ErrorResponse(
                        status = cause.statusCode,
                        message = cause.message
                    )
                )

                is BadRequestException, is ValidationException -> call.respond(
                    ErrorResponse(
                        status = 400,
                        message = cause.message.toString()
                    )
                )

                else -> call.respond(
                    ErrorResponse(
                        status = 500,
                        message = "An unexpected error occurred"
                    )
                )
            }
        }
    }

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(dev.urieloalves.studyconnect.infrastructure.shared.Env.JWT_SECRET))
                    .build()
            )

            validate { credential ->
                if (credential.payload.getClaim("id").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respond(
                    ErrorResponse(
                        status = 401,
                        message = "Token is not valid or has expired"
                    )
                )
            }
        }
    }

    routing {
        swaggerUI(path = "/api/docs", swaggerFile = "openapi/documentation.yaml")

        route("/api/v1") {
            oAuthRoutes()

            authenticate("auth-jwt") {
                groupRoutes()
            }
        }
    }
}

fun ApplicationCall.getUserIdFromToken(): UUID {
    val principal = this.principal<JWTPrincipal>()
    val id = principal?.payload?.getClaim("id")?.asString()
        ?: throw ClientException("Could not extract user id from JWT token")
    return UUID.fromString(id)
}
