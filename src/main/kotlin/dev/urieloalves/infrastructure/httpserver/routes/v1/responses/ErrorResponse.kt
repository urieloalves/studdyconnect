package dev.urieloalves.infrastructure.httpserver.routes.v1.responses

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: Int,
    val message: String
)