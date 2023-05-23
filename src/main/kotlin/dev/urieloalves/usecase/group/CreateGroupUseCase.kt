package dev.urieloalves.usecase.group

import dev.urieloalves.domain.group.factory.GroupFactory
import dev.urieloalves.domain.group.repository.GroupRepository
import dev.urieloalves.domain.user.repository.UserRepository
import dev.urieloalves.infrastructure.discord.DiscordClient
import dev.urieloalves.infrastructure.discord.dto.InputCreateChannelDto
import dev.urieloalves.infrastructure.shared.errors.CustomException
import dev.urieloalves.infrastructure.shared.errors.ServerException
import dev.urieloalves.usecase.group.dto.InputCreateGroupUseCaseDto
import io.ktor.server.plugins.NotFoundException
import org.slf4j.LoggerFactory

class CreateGroupUseCase(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val discordClient: DiscordClient,
) {

    private val logger = LoggerFactory.getLogger("CreateGroupUseCase")

    suspend fun execute(input: InputCreateGroupUseCaseDto) {
        try {
            val user =
                userRepository.findById(input.userId) ?: throw NotFoundException("User '${input.userId}' not found")

            val createChannelOutput = discordClient.createChannel(
                InputCreateChannelDto(
                    name = input.groupName,
                    description = input.groupDescription,
                    discordUserId = user.discordUser.id
                )
            )

            logger.info("Creating group for user '${input.userId}'")
            groupRepository.create(
                GroupFactory.create(
                    name = input.groupName,
                    description = input.groupDescription,
                    courseLink = input.courseLink,
                    createdBy = user.id,
                    discordChannelId = createChannelOutput.channelId
                )
            )
            logger.info("Group was successfully created by user '${input.userId}'")
        } catch (e: Exception) {
            val msg = "Could not create group by '${input.userId}'"
            logger.error(msg, e)
            when {
                e is CustomException -> throw e
                else -> throw ServerException(msg, e)
            }
        }
    }
}