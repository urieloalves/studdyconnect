package dev.urieloalves.application.group.dto

import java.util.UUID

data class InputGetGroupsByUserUseCaseDto(
    val userId: UUID
)

data class OutputGetGroupsByUserUseCaseDto(
    val id: UUID,
    val name: String,
    val description: String,
    val courseLink: String,
    val createdBy: UUID,
    val channelId: String
)