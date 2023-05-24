package dev.urieloalves.domain.group.repository

import dev.urieloalves.domain.group.entity.Group
import java.util.UUID

interface GroupRepository {
    fun create(entity: Group)
    fun findAllCreatedBy(id: UUID): List<Group>
    fun findById(id: UUID): Group?
    fun hasUserJoinedGroup(userId: UUID, groupId: UUID): Boolean
    fun joinGroup(userId: UUID, groupId: UUID)
    fun leaveGroup(userId: UUID, groupId: UUID)
    fun searchGroup(text: String): List<Group>
}