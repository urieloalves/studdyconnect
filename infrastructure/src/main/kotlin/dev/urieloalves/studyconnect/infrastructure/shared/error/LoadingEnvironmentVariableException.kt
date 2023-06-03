package dev.urieloalves.studyconnect.infrastructure.shared.error

class LoadingEnvironmentVariableException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message)