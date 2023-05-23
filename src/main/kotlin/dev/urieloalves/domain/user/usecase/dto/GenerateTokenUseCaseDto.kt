package dev.urieloalves.domain.user.usecase.dto

data class InputGenerateTokenUseCaseDto(val userId: String)

data class OutputGenerateTokenUseCaseDto(val token: String)