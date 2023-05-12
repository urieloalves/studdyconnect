package dev.urieloalves.data.dao

import dev.urieloalves.data.models.User
import dev.urieloalves.data.tables.UserTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID


interface UserDao {
    fun create(discordId: String, username: String, email: String)
    fun getByDiscordId(id: String): User?

    fun getById(id: String): User?
}

class UserDaoImpl : UserDao {

    override fun create(discordId: String, username: String, email: String) {
        val id = UUID.randomUUID().toString()
        transaction {
            UserTable.insert {
                it[UserTable.id] = id
                it[UserTable.discordId] = discordId
                it[UserTable.username] = username
                it[UserTable.email] = email
            }
        }
    }

    override fun getByDiscordId(id: String): User? {
        return transaction {
            UserTable
                .select { UserTable.discordId.eq(id) }
                .limit(1)
                .map { UserTable.toModel(it) }
                .firstOrNull()
        }
    }

    override fun getById(id: String): User? {
        return transaction {
            UserTable
                .select { UserTable.id.eq(id) }
                .limit(1)
                .map { UserTable.toModel(it) }
                .firstOrNull()
        }
    }

}
