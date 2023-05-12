package dev.urieloalves.services

import dev.urieloalves.data.dao.GroupDao
import dev.urieloalves.data.dao.GroupUserDaoImpl
import dev.urieloalves.data.dao.UserDao
import dev.urieloalves.data.models.Group
import dev.urieloalves.routes.v1.requests.CreateGroupRequest

class GroupService(
    val userDao: UserDao,
    val groupDao: GroupDao,
    val groupUserDao: GroupUserDaoImpl,
    val discordService: DiscordService
) {

    suspend fun createGroup(request: CreateGroupRequest, userId: String) {
        val user = userDao.getById(userId)

        val channelId = discordService.createChannel(
            name = request.name,
            description = request.description,
            discordId = user!!.discordId
        )

        groupDao.create(
            name = request.name,
            description = request.description,
            courseLink = request.courseLink,
            createdBy = user.id,
            channelId = channelId
        )
    }

    fun getGroups(userId: String): List<Group> {
        return groupDao.getAllCreatedBy(userId)
    }

    suspend fun joinGroup(groupId: String, userId: String) {
        val user = userDao.getById(userId)
        val group = groupDao.getById(groupId)
        group?.let {
            if (it.createdBy != userId) {
                val hasJoined = groupUserDao.hasUserJoinedGroup(userId = userId, groupId = groupId)
                if (!hasJoined) {
                    discordService.joinChannel(channelId = group.channelId, discordId = user!!.discordId)
                    groupUserDao.joinGroup(userId = userId, groupId = groupId)
                }
            }
        }
    }

    suspend fun leave(groupId: String, userId: String) {
        val user = userDao.getById(userId)
        val group = groupDao.getById(groupId)
        group?.let {
            if (it.createdBy != userId) {
                val hasJoined = groupUserDao.hasUserJoinedGroup(userId = userId, groupId = groupId)
                if (hasJoined) {
                    discordService.leaveChannel(channelId = group.channelId, discordId = user!!.discordId)
                    groupUserDao.leaveGroup(userId = userId, groupId = groupId)
                }
            }
        }
    }
}