package dev.urieloalves.clients.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAccessTokenResponse(
    @SerialName("access_token")
    val accessToken: String
)
