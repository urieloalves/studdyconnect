package dev.urieloalves.domain.group.repository

import dev.urieloalves.domain.group.entity.Group
import java.util.UUID

interface GroupRepository {
    fun create(entity: Group)
    fun findAllCreatedBy(id: UUID): List<Group>
    fun findById(id: String): Group?
    fun hasUserJoinedGroup(userId: String, groupId: String): Boolean
    fun joinGroup(userId: String, groupId: String)
    fun leaveGroup(userId: String, groupId: String)
    fun searchGroup(text: String): List<Group>
}