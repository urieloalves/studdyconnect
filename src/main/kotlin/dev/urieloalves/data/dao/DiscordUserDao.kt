package dev.urieloalves.data.dao

import dev.urieloalves.data.models.DiscordUser
import dev.urieloalves.data.tables.DiscordUsersTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction


interface DiscordUserDao {
    suspend fun create(id: String, username: String, email: String)
    suspend fun create(discordUser: DiscordUser)
    suspend fun getById(id: String): DiscordUser?
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

    override suspend fun create(discordUser: DiscordUser) {
        transaction {
            DiscordUsersTable.insert {
                fromModel(it, discordUser)
            }
        }
    }

    override suspend fun getById(id: String): DiscordUser? {
        return transaction {
            DiscordUsersTable
                .select { DiscordUsersTable.id.eq(id) }
                .limit(1)
                .map { DiscordUsersTable.toModel(it) }
                .firstOrNull()
        }
    }

}
