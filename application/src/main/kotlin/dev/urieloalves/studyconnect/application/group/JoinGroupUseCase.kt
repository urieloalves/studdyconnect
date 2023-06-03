package dev.urieloalves.application.group

import dev.urieloalves.application.group.dto.InputJoinGroupUseCaseDto
import dev.urieloalves.domain.group.repository.GroupRepository
import dev.urieloalves.domain.user.repository.UserRepository
import dev.urieloalves.infrastructure.discord.dto.InputJoinChannelDto
import dev.urieloalves.studyconnect.application.discord.DiscordClient
import org.slf4j.LoggerFactory

class JoinGroupUseCase(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val discordClient: DiscordClient
) {

    private val logger = LoggerFactory.getLogger("JoinGroupUseCase")

    suspend fun execute(input: InputJoinGroupUseCaseDto) {
        try {
            val user =
                userRepository.findById(input.userId) ?: throw Exception("User '${input.userId}' not found")
            val group =
                groupRepository.findById(input.groupId) ?: throw Exception("Group '${input.groupId}' not found")

            if (group.createdBy == input.userId) throw Exception("User '${input.userId}' cannot join its own group '${input.groupId}'")

            val hasJoined = groupRepository.hasUserJoinedGroup(userId = input.userId, groupId = input.groupId)
            if (hasJoined) throw Exception("User '${input.userId}' already joined group '${input.groupId}'")

            discordClient.joinChannel(
                InputJoinChannelDto(
                    channelId = group.discordChannel.id,
                    discordUserId = user.discordUser.id
                )
            )
            groupRepository.joinGroup(userId = input.userId, groupId = input.groupId)
            logger.info("User '${input.userId}' was successfully added to group '${input.groupId}'")
        } catch (e: Exception) {
            logger.error("User '${input.userId}' cold not join group '${input.groupId}'", e)
            throw e
        }

    }
}