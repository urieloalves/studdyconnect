package dev.urieloalves.data.tables

import dev.urieloalves.data.models.Group
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object GroupTable : Table("study_group") {
    val id = text("id").uniqueIndex()
    val name = text("name")
    val description = text("description")
    val courseLink = text("course_link")
    val createdById = text("created_by_id")
    val discordChannelId = long("discord_channel_id")

    fun fromModel(it: UpdateBuilder<Number>, group: Group) {
        it[id] = group.id
        it[name] = group.name
        it[description] = group.description
        it[courseLink] = group.courseLink
        it[createdById] = group.createdById
        it[discordChannelId] = group.discordChannelId
    }

    fun toModel(row: ResultRow): Group {
        return Group(
            id = row[id],
            name = row[name],
            description = row[description],
            courseLink = row[courseLink],
            createdById = row[createdById],
            discordChannelId = row[discordChannelId],
        )
    }
}