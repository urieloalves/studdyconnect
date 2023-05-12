package dev.urieloalves.data.tables

import dev.urieloalves.data.models.Channel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object ChannelTable : Table("channels") {
    val id = long("id").uniqueIndex()
    val guildId = long("guild_id")

    fun fromModel(it: UpdateBuilder<Number>, channel: Channel) {
        it[id] = channel.id
        it[guildId] = channel.guildId
    }

    fun toModel(row: ResultRow): Channel {
        return Channel(
            id = row[id],
            guildId = row[guildId],
        )
    }
}