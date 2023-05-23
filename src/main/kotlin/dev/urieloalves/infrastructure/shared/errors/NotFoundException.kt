package dev.urieloalves.infrastructure.shared.errors

class NotFoundException(override val message: String, override val cause: Throwable? = null) :
    CustomException(statusCode = 404, message = message, cause = cause)