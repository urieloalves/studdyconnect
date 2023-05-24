package dev.urieloalves.infrastructure.discord.dto

data class InputJoinChannelDto(
    val channelId: String,
    val discordUserId: String
)