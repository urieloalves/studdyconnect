package dev.urieloalves.application.group

import dev.urieloalves.application.group.dto.InputSearchGroupUseCaseDto
import dev.urieloalves.application.group.dto.OutputSearchGroupUseCaseDto
import dev.urieloalves.domain.group.repository.GroupRepository
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
            logger.error("An error occurred when searching for groups with text '${input.text}'", e)
            throw e
        }
    }
}