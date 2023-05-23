package dev.urieloalves.infrastructure.httpserver.v1.routes.responses

import kotlinx.serialization.Serializable

@Serializable
data class OAuthResponse(
    val token: String,
    val user: UserResponse
)