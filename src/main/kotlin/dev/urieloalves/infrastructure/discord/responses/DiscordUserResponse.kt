package dev.urieloalves.infrastructure.discord.responses

import kotlinx.serialization.Serializable

@Serializable
data class DiscordUserResponse(
    val id: String,
    val username: String,
    val email: String,
)
