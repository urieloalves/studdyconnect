package dev.urieloalves.data.dao

import dev.urieloalves.data.tables.ChannelTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction


interface ChannelDao {
    fun create(id: Long, guildId: Long)
}

class ChannelDaoImpl : ChannelDao {

    override fun create(id: Long, guildId: Long) {
        transaction {
            ChannelTable.insert {
                it[ChannelTable.id] = id
                it[ChannelTable.guildId] = guildId
            }
        }
    }
}
