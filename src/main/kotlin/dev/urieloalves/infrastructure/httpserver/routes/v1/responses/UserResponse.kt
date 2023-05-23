package dev.urieloalves.infrastructure.httpserver.routes.v1.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val username: String,
    val email: String
)