package dev.urieloalves.usecase.user.dto

import java.util.UUID

data class InputLeaveGroupUseCaseDto(
    val groupId: UUID,
    val userId: UUID
)
