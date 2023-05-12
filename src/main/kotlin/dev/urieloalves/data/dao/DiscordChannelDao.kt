package dev.urieloalves.data.dao

import dev.urieloalves.data.tables.DiscordChannelTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction


interface DiscordChannelCDao {
    suspend fun create(id: Long, guildId: Long)
}

class DiscordChannelDaoImpl : DiscordChannelCDao {

    override suspend fun create(id: Long, guildId: Long) {
        transaction {
            DiscordChannelTable.insert {
                it[DiscordChannelTable.id] = id
                it[DiscordChannelTable.guildId] = guildId
            }
        }
    }
}
