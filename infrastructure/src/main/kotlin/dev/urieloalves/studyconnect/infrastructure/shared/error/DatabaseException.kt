package dev.urieloalves.studyconnect.infrastructure.shared.error

class DatabaseException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message)