package dev.urieloalves.studyconnect.infrastructure.group.repository

import dev.urieloalves.domain.group.entity.Group
import dev.urieloalves.domain.group.valueobject.DiscordChannel
import dev.urieloalves.studyconnect.infrastructure.group.table.GroupTable
import dev.urieloalves.studyconnect.infrastructure.group.table.GroupUserTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import dev.urieloalves.domain.group.repository.GroupRepository as GroupRepositoryInterface

class GroupRepository : GroupRepositoryInterface {

    override fun create(entity: Group) {
        transaction {
            GroupTable.insert {
                it[id] = entity.id.toString()
                it[name] = entity.name
                it[description] = entity.description
                it[courseLink] = entity.courseLink
                it[createdBy] = entity.createdBy.toString()
                it[channelId] = entity.discordChannel.id
            }
        }
    }

    override fun findAllCreatedBy(id: UUID): List<Group> {
        return transaction {
            GroupTable.select { GroupTable.createdBy.eq(id.toString()) }
                .map {
                    Group(
                        id = UUID.fromString(it[GroupTable.id]),
                        name = it[GroupTable.name],
                        description = it[GroupTable.description],
                        courseLink = it[GroupTable.courseLink],
                        createdBy = UUID.fromString(it[GroupTable.createdBy]),
                        discordChannel = DiscordChannel(
                            id = it[GroupTable.channelId]
                        )
                    )
                }
        }
    }

    override fun findById(id: UUID): Group? {
        return transaction {
            GroupTable.select {
                GroupTable.id.eq(id.toString())
            }.limit(1)
                .map {
                    Group(
                        id = UUID.fromString(it[GroupTable.id]),
                        name = it[GroupTable.name],
                        description = it[GroupTable.description],
                        courseLink = it[GroupTable.courseLink],
                        createdBy = UUID.fromString(it[GroupTable.createdBy]),
                        discordChannel = DiscordChannel(
                            id = it[GroupTable.channelId]
                        )
                    )
                }
                .firstOrNull()
        }
    }

    override fun hasUserJoinedGroup(userId: UUID, groupId: UUID): Boolean {
        val exist = transaction {
            GroupUserTable.select {
                GroupUserTable.userId.eq(userId.toString()) and GroupUserTable.groupId.eq(groupId.toString())
            }.limit(1)
                .firstOrNull()
        }
        return exist != null
    }

    override fun joinGroup(userId: UUID, groupId: UUID) {
        transaction {
            GroupUserTable.insert {
                it[GroupUserTable.userId] = userId.toString()
                it[GroupUserTable.groupId] = groupId.toString()
            }
        }
    }

    override fun leaveGroup(userId: UUID, groupId: UUID) {
        transaction {
            GroupUserTable.deleteWhere {
                GroupUserTable.userId.eq(userId.toString()) and GroupUserTable.groupId.eq(groupId.toString())
            }
        }
    }

    override fun searchGroup(text: String): List<Group> {
        val lowercaseText = text.lowercase()
        return transaction {
            GroupTable.select {
                GroupTable.name.lowerCase().like("%$lowercaseText%") or
                        GroupTable.description.lowerCase().like("%$lowercaseText%")
            }.map {
                Group(
                    id = UUID.fromString(it[GroupTable.id]),
                    name = it[GroupTable.name],
                    description = it[GroupTable.description],
                    courseLink = it[GroupTable.courseLink],
                    createdBy = UUID.fromString(it[GroupTable.createdBy]),
                    discordChannel = DiscordChannel(
                        id = it[GroupTable.channelId]
                    )
                )
            }
        }
    }
}