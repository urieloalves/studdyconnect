package dev.urieloalves.infrastructure.discord.dto

import kotlinx.serialization.Serializable


data class InputGetUserDto(
    val token: String
)

@Serializable
data class OutputGetUserDto(
    val id: String,
    val username: String,
    val email: String,
)