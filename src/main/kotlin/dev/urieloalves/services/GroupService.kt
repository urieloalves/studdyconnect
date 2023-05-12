package dev.urieloalves.services

import dev.urieloalves.data.dao.GroupDao
import dev.urieloalves.data.dao.GroupUserDaoImpl
import dev.urieloalves.data.models.Group
import dev.urieloalves.routes.v1.requests.CreateGroupRequest

class GroupService(
    val groupDao: GroupDao,
    val groupUserDao: GroupUserDaoImpl,
    val discordService: DiscordService
) {

    suspend fun createGroup(request: CreateGroupRequest, userId: String) {

        discordService.createChannel(
            name = request.name,
            description = request.description,
            userId = userId
        )
//        val channelId = Instant.now().epochSecond
//        val guildId = Instant.now().epochSecond
//        channelDao.create(id = channelId, guildId = guildId)
//
//        groupDao.create(
//            name = request.name,
//            description = request.description,
//            courseLink = request.courseLink,
//            createdById = userId,
//            discordChannelId = channelId
//        )
    }

    fun getGroups(userId: String): List<Group> {
        return groupDao.getAllCreatedBy(userId)
    }

    fun joinGroup(groupId: String, userId: String) {
        val group = groupDao.getById(groupId)
        group?.let {
            if (it.createdBy != userId) {
                val hasJoined = groupUserDao.hasUserJoinedGroup(userId = userId, groupId = groupId)
                if (!hasJoined) {

                    groupUserDao.joinGroup(userId = userId, groupId = groupId)
                }
            }
        }
    }

    fun leave(groupId: String, userId: String) {
        val group = groupDao.getById(groupId)
        group?.let {
            if (it.createdBy != userId) {
                val hasJoined = groupUserDao.hasUserJoinedGroup(userId = userId, groupId = groupId)
                if (hasJoined) {
                    groupUserDao.leaveGroup(userId = userId, groupId = groupId)
                }
            }
        }
    }
}