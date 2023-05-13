package dev.urieloalves.data.models.errors

class NotFoundErrorException(override val message: String, override val cause: Throwable? = null) :
    CustomException(statusCode = 404, message = message, cause = cause)