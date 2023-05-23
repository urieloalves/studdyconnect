package dev.urieloalves.usecase.user.dto

import java.util.UUID

data class InputHandleOauthUseCaseDto(
    val code: String
)

data class OutputHandleOauthUseCaseDto(
    val userId: UUID,
    val username: String,
    val email: String,
    val token: String
)