package dev.urieloalves.infrastructure.discord.dto

data class InputLeaveChannelDto(
    val channelId: String,
    val discordUserId: String
)