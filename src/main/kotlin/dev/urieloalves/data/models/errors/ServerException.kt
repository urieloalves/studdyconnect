package dev.urieloalves.data.models.errors

class ServerException(override val message: String, override val cause: Throwable? = null) :
    CustomException(statusCode = 500, message = message, cause = cause)