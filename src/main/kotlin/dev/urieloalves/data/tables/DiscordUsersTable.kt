package dev.urieloalves.data.tables

import dev.urieloalves.data.models.DiscordUser
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object DiscordUsersTable : Table("discord_users") {
    val id = varchar("id", 255).uniqueIndex()
    val username = varchar("username", 255)
    val email = varchar("email", 255).uniqueIndex()
    override val primaryKey = PrimaryKey(id)

    fun fromModel(discordUser: DiscordUser) {

    }

    fun toModel(row: ResultRow): DiscordUser {
        return DiscordUser(
            id = row[id],
            username = row[username],
            email = row[email],
        )
    }
}