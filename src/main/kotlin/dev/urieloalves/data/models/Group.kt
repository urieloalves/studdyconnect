package dev.urieloalves.data.models

data class Group(
    val id: String,
    val name: String,
    val description: String,
    val courseLink: String,
    val createdBy: String,
    val channelId: String
)