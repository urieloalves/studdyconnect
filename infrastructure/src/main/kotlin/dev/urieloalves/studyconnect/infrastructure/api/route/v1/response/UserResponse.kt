package dev.urieloalves.studyconnect.infrastructure.api.route.v1.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val username: String,
    val email: String
)
