package dev.urieloalves.infrastructure.server.v1.routes.responses

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: Int,
    val message: String
)