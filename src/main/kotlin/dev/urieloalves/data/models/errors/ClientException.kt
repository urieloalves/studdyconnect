package dev.urieloalves.data.models.errors

class ClientException(override val message: String, override val cause: Throwable? = null) :
    CustomException(statusCode = 400, message = message, cause = cause)