package dev.urieloalves.infrastructure.httpserver.v1.routes.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val username: String,
    val email: String
)
