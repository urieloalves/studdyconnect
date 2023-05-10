package dev.urieloalves.routes.v1.responses

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)