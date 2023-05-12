package dev.urieloalves.data.dao

import dev.urieloalves.data.models.User
import dev.urieloalves.data.tables.UserTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction


interface UserDao {
    fun create(id: String, username: String, email: String)
    fun getById(id: String): User?
}

class UserDaoImpl : UserDao {

    override fun create(id: String, username: String, email: String) {
        transaction {
            UserTable.insert {
                it[UserTable.id] = id
                it[UserTable.username] = username
                it[UserTable.email] = email
            }
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
