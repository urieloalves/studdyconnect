package dev.urieloalves.infrastructure.api.route.v1.response

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
