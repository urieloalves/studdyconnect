package dev.urieloalves.data.dao

import dev.urieloalves.data.models.Group
import dev.urieloalves.data.tables.GroupTable
import dev.urieloalves.data.tables.GroupUserTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID


interface GroupDao {
    fun create(
        name: String,
        description: String,
        courseLink: String,
        createdBy: String,
        channelId: String
    )

    fun getAllCreatedBy(id: String): List<Group>
    fun getById(id: String): Group?
    fun hasUserJoinedGroup(userId: String, groupId: String): Boolean
    fun joinGroup(userId: String, groupId: String)
    fun leaveGroup(userId: String, groupId: String)
    fun searchGroup(text: String): List<Group>
}

class GroupDaoImpl : GroupDao {

    override fun create(
        name: String,
        description: String,
        courseLink: String,
        createdBy: String,
        channelId: String
    ) {
        val id = UUID.randomUUID().toString()
        transaction {
            GroupTable.insert {
                it[GroupTable.id] = id
                it[GroupTable.name] = name
                it[GroupTable.description] = description
                it[GroupTable.courseLink] = courseLink
                it[GroupTable.createdBy] = createdBy
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

    override fun hasUserJoinedGroup(userId: String, groupId: String): Boolean {
        val exist = transaction {
            GroupUserTable.select {
                GroupUserTable.userId.eq(userId) and GroupUserTable.groupId.eq(groupId)
            }.limit(1)
                .firstOrNull()
        }
        return exist != null
    }

    override fun joinGroup(userId: String, groupId: String) {
        transaction {
            GroupUserTable.insert {
                it[GroupUserTable.userId] = userId
                it[GroupUserTable.groupId] = groupId
            }
        }
    }

    override fun leaveGroup(userId: String, groupId: String) {
        transaction {
            GroupUserTable.deleteWhere {
                GroupUserTable.userId.eq(userId) and GroupUserTable.groupId.eq(groupId)
            }
        }
    }

    override fun searchGroup(text: String): List<Group> {
        val lowercaseText = text.lowercase()
        return transaction {
            GroupTable.select {
                GroupTable.name.lowerCase().like("%$lowercaseText%") or
                        GroupTable.description.lowerCase().like("%$lowercaseText%")
            }.map { GroupTable.toModel(it) }
        }
    }
}
