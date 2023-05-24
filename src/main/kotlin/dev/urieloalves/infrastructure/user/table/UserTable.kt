package dev.urieloalves.infrastructure.user.table

import org.jetbrains.exposed.sql.Table

object UserTable : Table("users") {
    val id = text("id").uniqueIndex()
    val discordId = text("discord_id").uniqueIndex()
    val username = text("username")
    val email = text("email").uniqueIndex()
}