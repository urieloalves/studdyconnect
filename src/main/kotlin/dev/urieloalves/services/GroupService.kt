package dev.urieloalves.services

import dev.urieloalves.data.dao.GroupDao
import dev.urieloalves.data.dao.UserDao
import dev.urieloalves.data.models.Group
import dev.urieloalves.data.models.errors.ClientException
import dev.urieloalves.data.models.errors.CustomException
import dev.urieloalves.data.models.errors.ServerException
import dev.urieloalves.routes.v1.requests.CreateGroupRequest
import io.ktor.server.plugins.NotFoundException
import org.slf4j.LoggerFactory

interface GroupService {
    suspend fun createGroup(request: CreateGroupRequest, userId: String)
    fun getGroups(userId: String): List<Group>
    suspend fun joinGroup(groupId: String, userId: String)
    suspend fun leaveGroup(groupId: String, userId: String)
    fun searchGroups(text: String): List<Group>
}

class GroupServiceImpl(
    val userDao: UserDao,
    val groupDao: GroupDao,
    val discordService: DiscordService
) : GroupService {

    private val logger = LoggerFactory.getLogger("GroupServiceImpl")

    override suspend fun createGroup(request: CreateGroupRequest, userId: String) {
        try {
            val user = userDao.getById(userId) ?: throw NotFoundException("User '${userId}' not found")

            val channelId = discordService.createChannel(
                name = request.name,
                description = request.description,
                discordId = user.discordId
            )

            logger.info("Creating group for user '$userId'")
            groupDao.create(
                name = request.name,
                description = request.description,
                courseLink = request.courseLink,
                createdBy = user.id,
                channelId = channelId
            )
            logger.info("Group was successfully created by user '$userId'")
        } catch (e: Exception) {
            val msg = "Could not create group by '$userId'"
            logger.error(msg, e)
            when {
                e is CustomException -> throw e
                else -> throw ServerException(msg, e)
            }
        }
    }

    override fun getGroups(userId: String): List<Group> {
        try {
            logger.info("Getting groups for user '$userId'")
            return groupDao.getAllCreatedBy(userId)
        } catch (e: Exception) {
            val msg = "An error occurred when getting groups for user '$userId'"
            logger.error(msg, e)
            throw ServerException(msg, e)
        }
    }

    override suspend fun joinGroup(groupId: String, userId: String) {
        try {
            val user = userDao.getById(userId) ?: throw NotFoundException("User '${userId}' not found")
            val group = groupDao.getById(groupId) ?: throw NotFoundException("Group '${groupId}' not found")

            if (group.createdBy == userId) throw ClientException("User '$userId' cannot join its own group '$groupId'")

            val hasJoined = groupDao.hasUserJoinedGroup(userId = userId, groupId = groupId)
            if (hasJoined) throw ClientException("User '$userId' already joined group '$groupId'")

            discordService.joinChannel(channelId = group.channelId, discordId = user!!.discordId)
            groupDao.joinGroup(userId = userId, groupId = groupId)
            logger.info("User '$userId' was successfully added to group '$groupId'")
        } catch (e: Exception) {
            val msg = "User '$userId' cold not join group '$groupId'"
            logger.error(msg, e)
            when {
                e is CustomException -> throw e
                else -> throw ServerException(msg, e)
            }
        }

    }

    override suspend fun leaveGroup(groupId: String, userId: String) {
        try {
            val user = userDao.getById(userId) ?: throw NotFoundException("User '${userId}' not found")

            val group = groupDao.getById(groupId) ?: throw NotFoundException("Group '${groupId}' not found")

            if (group.createdBy == userId) throw ClientException("User '$userId' cannot be removed from its own group '$groupId'")

            val hasJoined = groupDao.hasUserJoinedGroup(userId = userId, groupId = groupId)

            if (!hasJoined) throw ClientException("User '$user' is not a member of group '$groupId'")

            discordService.leaveChannel(channelId = group.channelId, discordId = user!!.discordId)
            groupDao.leaveGroup(userId = userId, groupId = groupId)
            logger.info("User '$userId' was successfully removed from group '$groupId'")
        } catch (e: Exception) {
            val msg = "User '$userId' could not be removed from group '$groupId'"
            logger.error(msg, e)
            when {
                e is CustomException -> throw e
                else -> throw ServerException(msg, e)
            }
        }
    }

    override fun searchGroups(text: String): List<Group> {
        try {
            logger.info("Searching groups with text '$text'")
            return groupDao.searchGroup(text)
        } catch (e: Exception) {
            val msg = "An error occurred when searching for groups with text '$text'"
            logger.error(msg, e)
            throw ServerException(msg, e)
        }

    }
}