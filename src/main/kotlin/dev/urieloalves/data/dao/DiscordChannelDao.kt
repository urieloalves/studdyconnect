package dev.urieloalves.data.dao

import dev.urieloalves.data.tables.DiscordChannelsTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction


interface DiscordChannelCDao {
    suspend fun create(id: Long, guildId: Long)
}

class DiscordChannelDaoImpl : DiscordChannelCDao {

    override suspend fun create(id: Long, guildId: Long) {
        transaction {
            DiscordChannelsTable.insert {
                it[DiscordChannelsTable.id] = id
                it[DiscordChannelsTable.guildId] = guildId
            }
        }
    }
}
