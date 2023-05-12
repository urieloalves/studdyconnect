package dev.urieloalves.data.tables

import dev.urieloalves.data.models.DiscordChannel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object DiscordChannelTable : Table("discord_channel") {
    val id = long("id").uniqueIndex()
    val guildId = long("guild_id")

    fun fromModel(it: UpdateBuilder<Number>, discordChannel: DiscordChannel) {
        it[id] = discordChannel.id
        it[guildId] = discordChannel.guildId
    }

    fun toModel(row: ResultRow): DiscordChannel {
        return DiscordChannel(
            id = row[id],
            guildId = row[guildId],
        )
    }
}