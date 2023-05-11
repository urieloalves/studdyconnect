package dev.urieloalves.data.dao

import dev.urieloalves.data.tables.DiscordUsersTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction


interface DiscordUserDao {
    suspend fun create(id: String, username: String, email: String)
}

class DiscordUserDaoImpl : DiscordUserDao {

    override suspend fun create(id: String, username: String, email: String) {
        transaction {
            DiscordUsersTable.insert {
                it[DiscordUsersTable.id] = id
                it[DiscordUsersTable.username] = username
                it[DiscordUsersTable.email] = email
            }
        }
    }

}
