package dev.urieloalves.infrastructure.api.route.v1.response

import kotlinx.serialization.Serializable

@Serializable
data class OAuthResponse(
    val token: String,
    val user: UserResponse
)