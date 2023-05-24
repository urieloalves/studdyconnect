package dev.urieloalves.usecase.group

import dev.urieloalves.domain.group.repository.GroupRepository
import dev.urieloalves.infrastructure.shared.errors.ServerException
import dev.urieloalves.usecase.group.dto.InputGetGroupsByUserUseCaseDto
import dev.urieloalves.usecase.group.dto.OutputGetGroupsByUserUseCaseDto
import org.slf4j.LoggerFactory

class GetGroupsByUserUseCase(
    private val groupRepository: GroupRepository
) {

    private val logger = LoggerFactory.getLogger("GetGroupsByUserUseCase")

    fun execute(input: InputGetGroupsByUserUseCaseDto): List<OutputGetGroupsByUserUseCaseDto> {
        try {
            logger.info("Getting groups for user '${input.userId}'")
            return groupRepository.findAllCreatedBy(input.userId).map {
                OutputGetGroupsByUserUseCaseDto(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    courseLink = it.courseLink,
                    createdBy = it.createdBy,
                    channelId = it.discordChannel.id
                )
            }
        } catch (e: Exception) {
            val msg = "An error occurred when getting groups for user '${input.userId}'"
            logger.error(msg, e)
            throw ServerException(msg, e)
        }
    }

}