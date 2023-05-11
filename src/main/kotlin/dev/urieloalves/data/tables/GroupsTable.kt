package dev.urieloalves.data.tables

import org.jetbrains.exposed.sql.Table

object GroupsTable : Table("groups") {
    val id = text("id").uniqueIndex()
    val name = text("name")
    val description = text("description")
    val courseLink = text("course_link")
    val createdById = text("created_by_id")
    val discordChannelId = text("discord_channel_id")
}