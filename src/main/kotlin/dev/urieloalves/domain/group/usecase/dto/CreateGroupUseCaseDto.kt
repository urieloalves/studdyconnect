package dev.urieloalves.domain.group.usecase.dto

import java.util.UUID

data class InputCreateGroupUseCaseDto(
    val groupName: String,
    val groupDescription: String,
    val courseLink: String,
    val userId: UUID,
)