package dev.urieloalves.data.dao

import dev.urieloalves.data.models.Group
import dev.urieloalves.data.tables.GroupTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID


interface GroupDao {
    fun create(
        name: String,
        description: String,
        courseLink: String,
        createdById: String,
        discordChannelId: Long
    )

    fun getAllCreatedBy(id: String): List<Group>

    fun getById(id: String): Group?
}

class GroupDaoImpl : GroupDao {

    override fun create(
        name: String,
        description: String,
        courseLink: String,
        createdById: String,
        channelId: Long
    ) {
        val id = UUID.randomUUID().toString()
        transaction {
            GroupTable.insert {
                it[GroupTable.id] = id
                it[GroupTable.name] = name
                it[GroupTable.description] = description
                it[GroupTable.courseLink] = courseLink
                it[GroupTable.createdBy] = createdById
                it[GroupTable.channelId] = channelId
            }
        }
    }

    override fun getAllCreatedBy(id: String): List<Group> {
        return transaction {
            GroupTable.select { GroupTable.createdBy.eq(id) }
                .map {
                    GroupTable.toModel(it)
                }
        }
    }

    override fun getById(id: String): Group? {
        return transaction {
            GroupTable.select {
                GroupTable.id.eq(id)
            }.limit(1)
                .map { GroupTable.toModel(it) }
                .firstOrNull()
        }
    }
}
