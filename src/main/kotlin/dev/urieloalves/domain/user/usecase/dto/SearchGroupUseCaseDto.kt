package dev.urieloalves.domain.user.usecase.dto

import java.util.UUID

class InputSearchGroupUseCaseDto(
    val text: String
)

class OutputSearchGroupUseCaseDto(
    val id: UUID,
    val name: String,
    val description: String,
    val courseLink: String,
    val createdBy: UUID,
    val channelId: String
)