package dev.urieloalves.data.tables

import org.jetbrains.exposed.sql.Table

object DiscordUsersTable : Table("discord_users") {
    val id = varchar("id", 255).uniqueIndex()
    val username = varchar("username", 255)
    val email = varchar("email", 255).uniqueIndex()
    
    override val primaryKey = PrimaryKey(id)
}