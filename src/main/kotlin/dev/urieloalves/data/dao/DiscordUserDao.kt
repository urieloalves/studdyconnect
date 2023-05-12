package dev.urieloalves.data.dao

import dev.urieloalves.data.models.DiscordUser
import dev.urieloalves.data.tables.DiscordUserTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction


interface DiscordUserDao {
    fun create(id: String, username: String, email: String)
    fun getById(id: String): DiscordUser?
}

class DiscordUserDaoImpl : DiscordUserDao {

    override fun create(id: String, username: String, email: String) {
        transaction {
            DiscordUserTable.insert {
                it[DiscordUserTable.id] = id
                it[DiscordUserTable.username] = username
                it[DiscordUserTable.email] = email
            }
        }
    }

    override fun getById(id: String): DiscordUser? {
        return transaction {
            DiscordUserTable
                .select { DiscordUserTable.id.eq(id) }
                .limit(1)
                .map { DiscordUserTable.toModel(it) }
                .firstOrNull()
        }
    }

}
