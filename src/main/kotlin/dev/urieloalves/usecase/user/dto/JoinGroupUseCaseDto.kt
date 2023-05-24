package dev.urieloalves.usecase.user.dto

import java.util.UUID

data class InputJoinGroupUseCaseDto(
    val groupId: UUID,
    val userId: UUID
)
