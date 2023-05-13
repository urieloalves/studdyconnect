package dev.urieloalves.clients

import dev.urieloalves.clients.responses.DiscordTokenResponse
import dev.urieloalves.clients.responses.DiscordUserResponse
import dev.urieloalves.configs.Env
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

interface DiscordClient {
    suspend fun getAccessToken(code: String): String
    suspend fun getUser(accessToken: String): DiscordUserResponse
}

class DiscordClientImpl : DiscordClient {
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override suspend fun getAccessToken(code: String): String {
        val data = httpClient.submitForm(
            url = "${Env.DISCORD_API_BASE_URL}/oauth2/token",
            formParameters = parameters {
                append("client_id", Env.DISCORD_CLIENT_ID)
                append("client_secret", Env.DISCORD_CLIENT_SECRET)
                append("grant_type", Env.DISCORD_GRANT_TYPE)
                append("code", code)
                append("redirect_uri", Env.DISCORD_MY_REDIRECT_URL)
            }
        ).body<DiscordTokenResponse>()

        return data.accessToken
    }

    override suspend fun getUser(accessToken: String): DiscordUserResponse {
        return httpClient.get("${Env.DISCORD_API_BASE_URL}/users/@me") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }.body<DiscordUserResponse>()
    }
}