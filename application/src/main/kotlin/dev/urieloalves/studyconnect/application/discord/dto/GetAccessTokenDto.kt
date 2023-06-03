package dev.urieloalves.infrastructure.discord.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class InputGetAccessTokenDto(
    val code: String
)

@Serializable
data class OutputGetAccessTokenDto(
    @SerialName("access_token")
    val accessToken: String
)