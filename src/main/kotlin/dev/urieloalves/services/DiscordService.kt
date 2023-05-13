package dev.urieloalves.services

import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.rest.service.createTextChannel
import dev.kord.rest.service.editMemberPermissions
import dev.kord.rest.service.editRolePermission
import dev.urieloalves.clients.DiscordClient
import dev.urieloalves.clients.responses.DiscordUserResponse
import dev.urieloalves.configs.Env
import dev.urieloalves.data.models.errors.CustomException


interface DiscordService {
    suspend fun getAccessToken(code: String): String
    suspend fun getUser(accessToken: String): DiscordUserResponse
    suspend fun joinServer(discordId: String, token: String)
    suspend fun createChannel(name: String, description: String, discordId: String): String
    suspend fun joinChannel(channelId: String, discordId: String)
    suspend fun leaveChannel(channelId: String, discordId: String)
}

class DiscordServiceImpl(
    val discordClient: DiscordClient
) : DiscordService {

    override suspend fun getAccessToken(code: String): String {
        try {
            return discordClient.getAccessToken(code)
        } catch (e: Exception) {
            throw CustomException(
                statusCode = 500,
                message = "Could not get discord access token",
                cause = e
            )
        }
    }

    override suspend fun getUser(accessToken: String): DiscordUserResponse {
        try {
            return discordClient.getUser(accessToken)
        } catch (e: Exception) {
            throw CustomException(
                statusCode = 500,
                message = "Could not get discord user",
                cause = e
            )
        }
    }

    override suspend fun joinServer(discordId: String, token: String) {
        try {
            val kord = Kord(Env.DISCORD_BOT_TOKEN)

            kord.rest.guild.addGuildMember(
                guildId = Snowflake(Env.DISCORD_GUILD_ID),
                userId = Snowflake(discordId),
                token = token
            ) {}
        } catch (e: Exception) {
            throw CustomException(
                statusCode = 500,
                message = "Could not add discord user '$discordId' to StudyConnect server",
                cause = e
            )
        }
    }

    override suspend fun createChannel(name: String, description: String, discordId: String): String {
        try {
            val kord = Kord(Env.DISCORD_BOT_TOKEN)

            val channel = kord.rest.guild.createTextChannel(
                guildId = Snowflake(Env.DISCORD_GUILD_ID),
                name = name
            ) {
                topic = description
            }

            kord.rest.channel.editRolePermission(
                channelId = channel.id,
                roleId = Snowflake(Env.DISCORD_ROLE_EVERYONE_ID)
            ) {
                denied = Permissions(Permission.All)
            }

            kord.rest.channel.editMemberPermissions(
                channelId = channel.id,
                memberId = Snowflake(discordId)
            ) {
                allowed = Permissions(Permission.All)
            }

            return channel.id.toString()
        } catch (e: Exception) {
            throw CustomException(
                statusCode = 500,
                message = "Could not create channel for discord user '$discordId'",
                cause = e
            )
        }
    }

    override suspend fun joinChannel(channelId: String, discordId: String) {
        try {
            val kord = Kord(Env.DISCORD_BOT_TOKEN)

            kord.rest.channel.editMemberPermissions(
                channelId = Snowflake(channelId),
                memberId = Snowflake(discordId)
            ) {
                allowed = Permissions(Permission.All)
            }
        } catch (e: Exception) {
            throw CustomException(
                statusCode = 500,
                message = "Could not add discord user '$discordId' to channel '$channelId'",
                cause = e
            )
        }
    }

    override suspend fun leaveChannel(channelId: String, discordId: String) {
        try {
            val kord = Kord(Env.DISCORD_BOT_TOKEN)

            kord.rest.channel.editMemberPermissions(
                channelId = Snowflake(channelId),
                memberId = Snowflake(discordId)
            ) {
                denied = Permissions(Permission.All)
            }
        } catch (e: Exception) {
            throw CustomException(
                statusCode = 500,
                message = "Could not remove discord user `$discordId` from channel '$channelId'",
                cause = e
            )
        }
    }
}