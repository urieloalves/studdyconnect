package dev.urieloalves.infrastructure.discord.dto

data class InputCreateChannelDto(
    val name: String,
    val description: String,
    val discordUserId: String
)

data class OutputCreateChannelDto(
    val channelId: String
)