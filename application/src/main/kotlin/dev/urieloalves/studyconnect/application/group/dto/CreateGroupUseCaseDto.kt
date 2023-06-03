package dev.urieloalves.application.group.dto

import java.util.UUID

data class InputCreateGroupUseCaseDto(
    val groupName: String,
    val groupDescription: String,
    val courseLink: String,
    val userId: UUID,
)