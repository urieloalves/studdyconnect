package dev.urieloalves.application.group.dto

import java.util.UUID

data class InputLeaveGroupUseCaseDto(
    val groupId: UUID,
    val userId: UUID
)
