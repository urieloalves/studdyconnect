package dev.urieloalves.data.tables

import dev.urieloalves.data.models.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object UserTable : Table("users") {
    val id = text("id").uniqueIndex()
    val username = text("username")
    val email = text("email").uniqueIndex()

    fun fromModel(it: UpdateBuilder<Number>, user: User) {
        it[id] = user.id
        it[username] = user.username
        it[email] = user.email
    }

    fun toModel(row: ResultRow): User {
        return User(
            id = row[id],
            username = row[username],
            email = row[email],
        )
    }
}