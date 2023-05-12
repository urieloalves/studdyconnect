package dev.urieloalves.data.tables

import dev.urieloalves.data.models.Group
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object GroupTable : Table("groups") {
    val id = text("id").uniqueIndex()
    val name = text("name")
    val description = text("description")
    val courseLink = text("course_link")
    val createdBy = text("created_by")
    val channelId = text("channel_id")

    fun fromModel(it: UpdateBuilder<Number>, group: Group) {
        it[id] = group.id
        it[name] = group.name
        it[description] = group.description
        it[courseLink] = group.courseLink
        it[createdBy] = group.createdBy
        it[channelId] = group.channelId
    }

    fun toModel(row: ResultRow): Group {
        return Group(
            id = row[id],
            name = row[name],
            description = row[description],
            courseLink = row[courseLink],
            createdBy = row[createdBy],
            channelId = row[channelId],
        )
    }
}