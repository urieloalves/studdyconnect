package dev.urieloalves.infrastructure.server.v1.routes.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupResponse(
    val id: String,
    val name: String,
    val description: String,
    @SerialName("course_link")
    val courseLink: String,
    @SerialName("created_by")
    val createdBy: String,
)
