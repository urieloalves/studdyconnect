package dev.urieloalves.clients

import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.rest.service.createTextChannel
import dev.kord.rest.service.editMemberPermissions
import dev.kord.rest.service.editRolePermission
import dev.urieloalves.clients.responses.DiscordTokenResponse
import dev.urieloalves.clients.responses.DiscordUserResponse
import dev.urieloalves.configs.Env
import dev.urieloalves.data.models.errors.DiscordException
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
import org.slf4j.LoggerFactory

interface DiscordClient {
    suspend fun getAccessToken(code: String): String
    suspend fun getUser(accessToken: String): DiscordUserResponse
    suspend fun joinServer(discordId: String, token: String)
    suspend fun createChannel(name: String, description: String, discordId: String): String
    suspend fun joinChannel(channelId: String, discordId: String)
    suspend fun leaveChannel(channelId: String, discordId: String)
}

class DiscordClientImpl : DiscordClient {

    private val logger = LoggerFactory.getLogger("DiscordClientImpl")

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private val discordBotToken = Env.DISCORD_BOT_TOKEN

    private val discordGuildId = Env.DISCORD_GUILD_ID

    private val discordRoleEveryoneId = Env.DISCORD_ROLE_EVERYONE_ID

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

    override suspend fun joinServer(discordId: String, token: String) {
        try {
            val kord = Kord(discordBotToken)

            logger.info("Adding discord user '$discordId' to StudyConnect discord server")
            kord.rest.guild.addGuildMember(
                guildId = Snowflake(discordGuildId),
                userId = Snowflake(discordId),
                token = token
            ) {}
            logger.info("Discord user '$discordId' was successfully added to StudyConnect discord server")
        } catch (e: Exception) {
            val msg = "Could not add discord user '$discordId' to StudyConnect server"
            logger.error(msg, e)
            throw DiscordException(msg, e)
        }
    }

    override suspend fun createChannel(name: String, description: String, discordId: String): String {
        try {
            val kord = Kord(discordBotToken)

            logger.info("Creating discord channel by discord user '$discordId'")
            val channel = kord.rest.guild.createTextChannel(
                guildId = Snowflake(discordGuildId),
                name = name
            ) {
                topic = description
            }
            logger.info("Discord channel '${channel.id}' was successfully created by discord user '$discordId'")

            logger.info("Hiding discord channel `${channel.id}` from everyone")
            kord.rest.channel.editRolePermission(
                channelId = channel.id,
                roleId = Snowflake(discordRoleEveryoneId)
            ) {
                denied = Permissions(Permission.All)
            }
            logger.info("Discord channel was successfully hidden from everyone")

            logger.info("Allowing discord user '$discordId' to use discord channel `${channel.id}`")
            kord.rest.channel.editMemberPermissions(
                channelId = channel.id,
                memberId = Snowflake(discordId)
            ) {
                allowed = Permissions(Permission.All)
            }
            logger.info("Discord user '$discordId' is now allowed to use discord channel '${channel.id}'")

            return channel.id.toString()
        } catch (e: Exception) {
            val msg = "Could not create channel for discord user '$discordId'"
            logger.error(msg, e)
            throw DiscordException(msg, e)
        }
    }

    override suspend fun joinChannel(channelId: String, discordId: String) {
        try {
            val kord = Kord(discordBotToken)

            logger.info("Allowing discord user '$discordId' to use discord channel '$channelId'")
            kord.rest.channel.editMemberPermissions(
                channelId = Snowflake(channelId),
                memberId = Snowflake(discordId)
            ) {
                allowed = Permissions(Permission.All)
            }
            logger.info("Discord user '$discordId' is now allowed to use discord channel '$discordId'")
        } catch (e: Exception) {
            val msg = "Could not add discord user '$discordId' to discord channel '$channelId'"
            logger.error(msg, e)
            throw DiscordException(msg, e)
        }
    }

    override suspend fun leaveChannel(channelId: String, discordId: String) {
        try {
            val kord = Kord(discordBotToken)

            logger.info("Hiding channel '$channelId' from discord user '$discordId'")
            kord.rest.channel.editMemberPermissions(
                channelId = Snowflake(channelId),
                memberId = Snowflake(discordId)
            ) {
                denied = Permissions(Permission.All)
            }
            logger.info("Discord channel '$channelId' was successfully hidden from discord user '$discordId'")
        } catch (e: Exception) {
            val msg = "Could not remove discord user `$discordId` from channel '$channelId'"
            logger.error(msg, e)
            throw DiscordException(msg, e)
        }
    }
}