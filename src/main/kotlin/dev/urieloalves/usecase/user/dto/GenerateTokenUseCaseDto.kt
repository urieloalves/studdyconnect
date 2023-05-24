package dev.urieloalves.usecase.user.dto

import java.util.UUID

data class InputGenerateTokenUseCaseDto(val userId: UUID)

data class OutputGenerateTokenUseCaseDto(val token: String)