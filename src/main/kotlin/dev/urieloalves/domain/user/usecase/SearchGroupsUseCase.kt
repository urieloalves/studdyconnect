package dev.urieloalves.domain.user.usecase

import dev.urieloalves.domain.group.repository.GroupRepository
import dev.urieloalves.domain.user.usecase.dto.InputSearchGroupUseCaseDto
import dev.urieloalves.domain.user.usecase.dto.OutputSearchGroupUseCaseDto
import dev.urieloalves.infrastructure.shared.errors.ServerException
import org.slf4j.LoggerFactory

class SearchGroupsUseCase(
    private val groupRepository: GroupRepository
) {

    private val logger = LoggerFactory.getLogger("SearchGroupsUseCase")

    fun execute(input: InputSearchGroupUseCaseDto): List<OutputSearchGroupUseCaseDto> {
        try {
            logger.info("Searching groups with text '${input.text}'")
            return groupRepository.searchGroup(input.text).map {
                OutputSearchGroupUseCaseDto(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    courseLink = it.courseLink,
                    createdBy = it.createdBy,
                    channelId = it.discordChannel.id
                )
            }
        } catch (e: Exception) {
            val msg = "An error occurred when searching for groups with text '${input.text}'"
            logger.error(msg, e)
            throw ServerException(msg, e)
        }

    }
}