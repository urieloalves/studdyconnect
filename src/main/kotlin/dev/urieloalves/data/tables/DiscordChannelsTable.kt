package dev.urieloalves.data.tables

import org.jetbrains.exposed.sql.Table

object DiscordChannelsTable : Table("groups") {
    val id = long("id").uniqueIndex()
    val guildId = long("guild_id")
}