package dev.urieloalves.usecase.user

import dev.urieloalves.domain.group.repository.GroupRepository
import dev.urieloalves.domain.user.repository.UserRepository
import dev.urieloalves.infrastructure.discord.DiscordClient
import dev.urieloalves.infrastructure.discord.dto.InputLeaveChannelDto
import dev.urieloalves.infrastructure.httpserver.error.ClientException
import dev.urieloalves.usecase.user.dto.InputLeaveGroupUseCaseDto
import io.ktor.server.plugins.NotFoundException
import org.slf4j.LoggerFactory

class LeaveGroupUseCase(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val discordClient: DiscordClient
) {

    private val logger = LoggerFactory.getLogger("LeaveGroupUseCase")

    suspend fun execute(input: InputLeaveGroupUseCaseDto) {
        try {
            val user =
                userRepository.findById(input.userId) ?: throw NotFoundException("User '${input.userId}' not found")

            val group =
                groupRepository.findById(input.groupId) ?: throw NotFoundException("Group '${input.groupId}' not found")

            if (group.createdBy == input.userId) throw ClientException("User '${input.userId}' cannot be removed from its own group '${input.groupId}'")

            val hasJoined = groupRepository.hasUserJoinedGroup(userId = input.userId, groupId = input.groupId)

            if (!hasJoined) throw ClientException("User '$user' is not a member of group '${input.groupId}'")

            discordClient.leaveChannel(
                InputLeaveChannelDto(
                    channelId = group.discordChannel.id,
                    discordUserId = user.discordUser.id
                )
            )
            groupRepository.leaveGroup(userId = input.userId, groupId = input.groupId)
            logger.info("User '${input.userId}' was successfully removed from group '${input.groupId}'")
        } catch (e: Exception) {
            logger.error("User '${input.userId}' could not be removed from group '${input.groupId}'", e)
            throw e
        }
    }
}