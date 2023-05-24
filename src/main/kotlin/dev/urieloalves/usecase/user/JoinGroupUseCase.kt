package dev.urieloalves.usecase.user

import dev.urieloalves.domain.group.repository.GroupRepository
import dev.urieloalves.domain.user.repository.UserRepository
import dev.urieloalves.infrastructure.discord.DiscordClient
import dev.urieloalves.infrastructure.discord.dto.InputJoinChannelDto
import dev.urieloalves.infrastructure.httpserver.error.ClientException
import dev.urieloalves.usecase.user.dto.InputJoinGroupUseCaseDto
import io.ktor.server.plugins.NotFoundException
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
                userRepository.findById(input.userId) ?: throw NotFoundException("User '${input.userId}' not found")
            val group =
                groupRepository.findById(input.groupId) ?: throw NotFoundException("Group '${input.groupId}' not found")

            if (group.createdBy == input.userId) throw ClientException("User '${input.userId}' cannot join its own group '${input.groupId}'")

            val hasJoined = groupRepository.hasUserJoinedGroup(userId = input.userId, groupId = input.groupId)
            if (hasJoined) throw ClientException("User '${input.userId}' already joined group '${input.groupId}'")

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