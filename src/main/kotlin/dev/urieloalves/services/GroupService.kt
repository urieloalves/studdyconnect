package dev.urieloalves.services

import dev.urieloalves.data.dao.GroupDao
import dev.urieloalves.data.dao.UserDao
import dev.urieloalves.data.models.Group
import dev.urieloalves.routes.v1.requests.CreateGroupRequest

interface GroupService {
    suspend fun createGroup(request: CreateGroupRequest, userId: String)
    fun getGroups(userId: String): List<Group>
    suspend fun joinGroup(groupId: String, userId: String)
    suspend fun leaveGroup(groupId: String, userId: String)
}

class GroupServiceImpl(
    val userDao: UserDao,
    val groupDao: GroupDao,
    val discordService: DiscordService
) : GroupService {

    override suspend fun createGroup(request: CreateGroupRequest, userId: String) {
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

    override fun getGroups(userId: String): List<Group> {
        return groupDao.getAllCreatedBy(userId)
    }

    override suspend fun joinGroup(groupId: String, userId: String) {
        val user = userDao.getById(userId)
        val group = groupDao.getById(groupId)
        group?.let {
            if (it.createdBy != userId) {
                val hasJoined = groupDao.hasUserJoinedGroup(userId = userId, groupId = groupId)
                if (!hasJoined) {
                    discordService.joinChannel(channelId = group.channelId, discordId = user!!.discordId)
                    groupDao.joinGroup(userId = userId, groupId = groupId)
                }
            }
        }
    }

    override suspend fun leaveGroup(groupId: String, userId: String) {
        val user = userDao.getById(userId)
        val group = groupDao.getById(groupId)
        group?.let {
            if (it.createdBy != userId) {
                val hasJoined = groupDao.hasUserJoinedGroup(userId = userId, groupId = groupId)
                if (hasJoined) {
                    discordService.leaveChannel(channelId = group.channelId, discordId = user!!.discordId)
                    groupDao.leaveGroup(userId = userId, groupId = groupId)
                }
            }
        }
    }
}