package dev.urieloalves.clients.responses

import kotlinx.serialization.Serializable

@Serializable
data class DiscordUserResponse(
    val id: String,
    val username: String,
    val email: String,
)
