package dev.urieloalves.infrastructure.httpserver.route.v1.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: Int,
    val message: String
)