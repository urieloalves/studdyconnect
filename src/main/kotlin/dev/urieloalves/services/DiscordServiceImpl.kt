package dev.urieloalves.services

import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.rest.service.createTextChannel
import dev.kord.rest.service.editMemberPermissions
import dev.kord.rest.service.editRolePermission
import dev.urieloalves.configs.Env


interface DiscordService {
    suspend fun joinServer(discordId: String, token: String)
    suspend fun createChannel(name: String, description: String, discordId: String): String

    suspend fun joinChannel(channelId: String, discordId: String)

    suspend fun leaveChannel(channelId: String, discordId: String)
}

class DiscordServiceImpl : DiscordService {

    override suspend fun joinServer(discordId: String, token: String) {
        val kord = Kord(Env.DISCORD_BOT_TOKEN)

        kord.rest.guild.addGuildMember(
            guildId = Snowflake(Env.DISCORD_GUILD_ID),
            userId = Snowflake(discordId),
            token = token
        ) {}
    }

    override suspend fun createChannel(name: String, description: String, discordId: String): String {
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
    }

    override suspend fun joinChannel(channelId: String, discordId: String) {
        val kord = Kord(Env.DISCORD_BOT_TOKEN)

        kord.rest.channel.editMemberPermissions(
            channelId = Snowflake(channelId),
            memberId = Snowflake(discordId)
        ) {
            allowed = Permissions(Permission.All)
        }
    }

    override suspend fun leaveChannel(channelId: String, discordId: String) {
        val kord = Kord(Env.DISCORD_BOT_TOKEN)

        kord.rest.channel.editMemberPermissions(
            channelId = Snowflake(channelId),
            memberId = Snowflake(discordId)
        ) {
            denied = Permissions(Permission.All)
        }
    }
}