package dev.urieloalves.infrastructure.group.table

import org.jetbrains.exposed.sql.Table

object GroupTable : Table("groups") {
    val id = text("id").uniqueIndex()
    val name = text("name")
    val description = text("description")
    val courseLink = text("course_link")
    val createdBy = text("created_by")
    val channelId = text("channel_id")
}