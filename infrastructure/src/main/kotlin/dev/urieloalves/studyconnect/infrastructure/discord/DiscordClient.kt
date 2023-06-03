package dev.urieloalves.studyconnect.infrastructure.discord

import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.rest.service.createTextChannel
import dev.kord.rest.service.editMemberPermissions
import dev.kord.rest.service.editRolePermission
import dev.urieloalves.infrastructure.discord.dto.*
import dev.urieloalves.infrastructure.discord.error.DiscordClientException
import dev.urieloalves.studyconnect.application.discord.DiscordClient
import dev.urieloalves.studyconnect.infrastructure.shared.Env
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory


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

    override suspend fun getAccessToken(input: InputGetAccessTokenDto): OutputGetAccessTokenDto {
        return httpClient.submitForm(
            url = "${Env.DISCORD_API_BASE_URL}/oauth2/token",
            formParameters = parameters {
                append("client_id", Env.DISCORD_CLIENT_ID)
                append("client_secret", Env.DISCORD_CLIENT_SECRET)
                append("grant_type", Env.DISCORD_GRANT_TYPE)
                append("code", input.code)
                append("redirect_uri", Env.DISCORD_MY_REDIRECT_URL)
            }
        ).body<OutputGetAccessTokenDto>()
    }

    override suspend fun getUser(input: InputGetUserDto): OutputGetUserDto {
        return httpClient.get("${Env.DISCORD_API_BASE_URL}/users/@me") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${input.token}")
            }
        }.body<OutputGetUserDto>()
    }

    override suspend fun joinServer(input: InputJoinServerDto) {
        try {
            val kord = Kord(discordBotToken)

            logger.info("Adding discord user '${input.discordUserId}' to StudyConnect discord server")
            kord.rest.guild.addGuildMember(
                guildId = Snowflake(discordGuildId),
                userId = Snowflake(input.discordUserId),
                token = input.token
            ) {}
            logger.info("Discord user '${input.discordUserId}' was successfully added to StudyConnect discord server")
        } catch (e: Exception) {
            val msg = "Could not add discord user '${input.discordUserId}' to StudyConnect server"
            logger.error(msg, e)
            throw DiscordClientException(msg, e)
        }
    }

    override suspend fun createChannel(input: InputCreateChannelDto): OutputCreateChannelDto {
        try {
            val kord = Kord(discordBotToken)

            logger.info("Creating discord channel by discord user '${input.discordUserId}'")
            val channel = kord.rest.guild.createTextChannel(
                guildId = Snowflake(discordGuildId),
                name = input.name
            ) {
                topic = input.description
            }
            logger.info("Discord channel '${channel.id}' was successfully created by discord user '${input.discordUserId}'")

            logger.info("Hiding discord channel `${channel.id}` from everyone")
            kord.rest.channel.editRolePermission(
                channelId = channel.id,
                roleId = Snowflake(discordRoleEveryoneId)
            ) {
                denied = Permissions(Permission.All)
            }
            logger.info("Discord channel was successfully hidden from everyone")

            logger.info("Allowing discord user '${input.discordUserId}' to use discord channel `${channel.id}`")
            kord.rest.channel.editMemberPermissions(
                channelId = channel.id,
                memberId = Snowflake(input.discordUserId)
            ) {
                allowed = Permissions(Permission.All)
            }
            logger.info("Discord user '${input.discordUserId}' is now allowed to use discord channel '${channel.id}'")

            return OutputCreateChannelDto(
                channelId = channel.id.toString()
            )
        } catch (e: Exception) {
            val msg = "Could not create channel for discord user '${input.discordUserId}'"
            logger.error(msg, e)
            throw DiscordClientException(msg, e)
        }
    }

    override suspend fun joinChannel(input: InputJoinChannelDto) {
        try {
            val kord = Kord(discordBotToken)

            logger.info("Allowing discord user '${input.discordUserId}' to use discord channel '${input.channelId}'")
            kord.rest.channel.editMemberPermissions(
                channelId = Snowflake(input.channelId),
                memberId = Snowflake(input.discordUserId)
            ) {
                allowed = Permissions(Permission.All)
            }
            logger.info("Discord user '${input.discordUserId}' is now allowed to use discord channel '${input.channelId}'")
        } catch (e: Exception) {
            val msg = "Could not add discord user '${input.discordUserId}' to discord channel '${input.channelId}'"
            logger.error(msg, e)
            throw DiscordClientException(msg, e)
        }
    }

    override suspend fun leaveChannel(input: InputLeaveChannelDto) {
        try {
            val kord = Kord(discordBotToken)

            logger.info("Hiding channel '${input.channelId}' from discord user '${input.discordUserId}'")
            kord.rest.channel.editMemberPermissions(
                channelId = Snowflake(input.channelId),
                memberId = Snowflake(input.discordUserId)
            ) {
                denied = Permissions(Permission.All)
            }
            logger.info("Discord channel '${input.channelId}' was successfully hidden from discord user '${input.discordUserId}'")
        } catch (e: Exception) {
            val msg = "Could not remove discord user `${input.discordUserId}` from channel '${input.channelId}'"
            logger.error(msg, e)
            throw DiscordClientException(msg, e)
        }
    }
}