package dev.urieloalves.infrastructure.discord.dto

data class InputJoinServerDto(
    val discordUserId: String,
    val token: String
)