package dev.urieloalves.data.dao

import dev.urieloalves.data.tables.GroupsTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID


interface GroupDao {
    suspend fun create(
        name: String,
        description: String,
        courseLink: String,
        createdById: String,
        discordChannelId: Long
    )
}

class GroupDaoImpl : GroupDao {

    override suspend fun create(
        name: String,
        description: String,
        courseLink: String,
        createdById: String,
        discordChannelId: Long
    ) {
        val id = UUID.randomUUID().toString()
        transaction {
            GroupsTable.insert {
                it[GroupsTable.id] = id
                it[GroupsTable.name] = name
                it[GroupsTable.description] = description
                it[GroupsTable.courseLink] = courseLink
                it[GroupsTable.createdById] = createdById
                it[GroupsTable.discordChannelId] = discordChannelId
            }
        }
    }
}
