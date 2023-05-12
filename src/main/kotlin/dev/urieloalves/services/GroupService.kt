package dev.urieloalves.services

import dev.urieloalves.data.dao.DiscordChannelCDao
import dev.urieloalves.data.dao.GroupDao
import dev.urieloalves.routes.v1.requests.CreateGroupRequest

class GroupService(
    val groupDao: GroupDao,
    val discordChannelDao: DiscordChannelCDao
) {

    suspend fun createGroup(request: CreateGroupRequest, userId: String) {
        val channelId = 1L
        val guildId = 1L
        discordChannelDao.create(id = channelId, guildId = guildId)

        groupDao.create(
            name = request.name,
            description = request.description,
            courseLink = request.courseLink,
            createdById = userId,
            discordChannelId = channelId
        )
    }
}