package dev.urieloalves.infrastructure.server.v1.routes.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateGroupRequest(
    val name: String,
    val description: String,
    @SerialName("course_link")
    val courseLink: String
)