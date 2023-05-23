package dev.urieloalves.domain.user.usecase

import dev.urieloalves.domain.group.repository.GroupRepository
import dev.urieloalves.domain.user.repository.UserRepository
import dev.urieloalves.domain.user.usecase.dto.InputLeaveGroupUseCaseDto
import dev.urieloalves.infrastructure.discord.DiscordClient
import dev.urieloalves.infrastructure.discord.dto.InputLeaveChannelDto
import dev.urieloalves.infrastructure.shared.errors.ClientException
import dev.urieloalves.infrastructure.shared.errors.CustomException
import dev.urieloalves.infrastructure.shared.errors.ServerException
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
            val msg = "User '${input.userId}' could not be removed from group '${input.groupId}'"
            logger.error(msg, e)
            when {
                e is CustomException -> throw e
                else -> throw ServerException(msg, e)
            }
        }
    }
}