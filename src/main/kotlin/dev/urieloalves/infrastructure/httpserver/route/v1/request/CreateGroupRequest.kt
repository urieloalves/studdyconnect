package dev.urieloalves.infrastructure.httpserver.route.v1.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateGroupRequest(
    val name: String,
    val description: String,
    @SerialName("course_link")
    val courseLink: String
)