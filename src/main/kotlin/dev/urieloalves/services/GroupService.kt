package dev.urieloalves.services

import dev.urieloalves.data.dao.DiscordChannelCDao
import dev.urieloalves.data.dao.GroupDao
import dev.urieloalves.data.dao.GroupUserDaoImpl
import dev.urieloalves.data.models.Group
import dev.urieloalves.routes.v1.requests.CreateGroupRequest
import java.time.Instant

class GroupService(
    val groupDao: GroupDao,
    val discordChannelDao: DiscordChannelCDao,
    val groupUserDao: GroupUserDaoImpl
) {

    suspend fun createGroup(request: CreateGroupRequest, userId: String) {
        val channelId = Instant.now().epochSecond
        val guildId = Instant.now().epochSecond
        discordChannelDao.create(id = channelId, guildId = guildId)

        groupDao.create(
            name = request.name,
            description = request.description,
            courseLink = request.courseLink,
            createdById = userId,
            discordChannelId = channelId
        )
    }

    suspend fun getGroups(userId: String): List<Group> {
        return groupDao.getAllCreatedBy(userId)
    }

    fun joinGroup(groupId: String, userId: String) {
        val group = groupDao.getById(groupId)
        group?.let {
            if (it.createdById != userId) {
                val hasJoined = groupUserDao.hasUserJoinedGroup(userId = userId, groupId = groupId)
                if (!hasJoined) {
                    groupUserDao.joinGroup(userId = userId, groupId = groupId)
                }
            }
        }
    }
}