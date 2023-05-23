package dev.urieloalves.infrastructure.shared.errors

class ClientException(override val message: String, override val cause: Throwable? = null) :
    CustomException(statusCode = 400, message = message, cause = cause)
