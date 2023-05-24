package dev.urieloalves.infrastructure.shared.error

class DatabaseException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message)