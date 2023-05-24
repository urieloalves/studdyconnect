package dev.urieloalves.usecase.group.dto

import java.util.UUID

data class InputJoinGroupUseCaseDto(
    val groupId: UUID,
    val userId: UUID
)
