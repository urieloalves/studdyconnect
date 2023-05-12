package dev.urieloalves.services

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.urieloalves.configs.Env

class DiscordService {

    suspend fun joinServer(userId: String, token: String) {
        val kord = Kord(Env.DISCORD_BOT_TOKEN)
        kord.rest.guild.addGuildMember(
            guildId = Snowflake(Env.DISCORD_GUILD_ID),
            userId = Snowflake(userId.toLong()),
            token = token
        ) {}
    }
}