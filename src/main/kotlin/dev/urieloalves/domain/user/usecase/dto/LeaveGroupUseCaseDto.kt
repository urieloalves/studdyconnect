package dev.urieloalves.domain.user.usecase.dto

import java.util.UUID

data class InputLeaveGroupUseCaseDto(
    val groupId: UUID,
    val userId: UUID
)
