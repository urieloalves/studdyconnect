package dev.urieloalves.data.dao

import dev.urieloalves.data.models.Group
import dev.urieloalves.data.tables.GroupTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
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

    suspend fun getByCreatedById(id: String): List<Group>
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
            GroupTable.insert {
                it[GroupTable.id] = id
                it[GroupTable.name] = name
                it[GroupTable.description] = description
                it[GroupTable.courseLink] = courseLink
                it[GroupTable.createdById] = createdById
                it[GroupTable.discordChannelId] = discordChannelId
            }
        }
    }

    override suspend fun getByCreatedById(id: String): List<Group> {
        return transaction {
            GroupTable.select { GroupTable.createdById.eq(id) }
                .map {
                    GroupTable.toModel(it)
                }
        }
    }
}
