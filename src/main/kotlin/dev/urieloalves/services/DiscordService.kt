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
import dev.urieloalves.data.models.errors.DiscordException
import org.slf4j.LoggerFactory


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

    private val logger = LoggerFactory.getLogger("DiscordServiceImpl")

    override suspend fun getAccessToken(code: String): String {
        try {
            return discordClient.getAccessToken(code)
        } catch (e: Exception) {
            val msg = "Could not get discord access token"
            logger.error(msg, e)
            throw DiscordException(msg, e)
        }
    }

    override suspend fun getUser(accessToken: String): DiscordUserResponse {
        try {
            return discordClient.getUser(accessToken)
        } catch (e: Exception) {
            val msg = "Could not get discord user"
            logger.error(msg, e)
            throw DiscordException(msg, e)
        }
    }

    override suspend fun joinServer(discordId: String, token: String) {
        try {
            val kord = Kord(Env.DISCORD_BOT_TOKEN)

            logger.info("Adding discord user '$discordId' to StudyConnect discord server")
            kord.rest.guild.addGuildMember(
                guildId = Snowflake(Env.DISCORD_GUILD_ID),
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
            val kord = Kord(Env.DISCORD_BOT_TOKEN)

            logger.info("Creating discord channel by discord user '$discordId'")
            val channel = kord.rest.guild.createTextChannel(
                guildId = Snowflake(Env.DISCORD_GUILD_ID),
                name = name
            ) {
                topic = description
            }
            logger.info("Discord channel '${channel.id}' was successfully created by discord user '$discordId'")

            logger.info("Hiding discord channel `${channel.id}` from everyone")
            kord.rest.channel.editRolePermission(
                channelId = channel.id,
                roleId = Snowflake(Env.DISCORD_ROLE_EVERYONE_ID)
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
            val kord = Kord(Env.DISCORD_BOT_TOKEN)

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
            val kord = Kord(Env.DISCORD_BOT_TOKEN)

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