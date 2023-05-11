package dev.urieloalves.data.tables

import org.jetbrains.exposed.sql.Table

object DiscordChannelsTable : Table("groups") {
    val id = text("id").uniqueIndex()
    val username = text("username")
    val email = text("email").uniqueIndex()
}