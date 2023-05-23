package dev.urieloalves.infrastructure.httpserver.routes.v1.responses

import kotlinx.serialization.Serializable

@Serializable
data class OAuthResponse(
    val token: String,
    val user: UserResponse
)