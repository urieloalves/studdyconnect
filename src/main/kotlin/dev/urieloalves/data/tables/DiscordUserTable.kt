package dev.urieloalves.data.tables

import dev.urieloalves.data.models.DiscordUser
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object DiscordUserTable : Table("users") {
    val id = text("id").uniqueIndex()
    val username = text("username")
    val email = text("email").uniqueIndex()

    fun fromModel(it: UpdateBuilder<Number>, discordUser: DiscordUser) {
        it[id] = discordUser.id
        it[username] = discordUser.username
        it[email] = discordUser.email
    }

    fun toModel(row: ResultRow): DiscordUser {
        return DiscordUser(
            id = row[id],
            username = row[username],
            email = row[email],
        )
    }
}