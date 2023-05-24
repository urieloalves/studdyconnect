package dev.urieloalves.domain.shared.error

open class ValidationException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message)