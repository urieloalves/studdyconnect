package dev.urieloalves.data.models.errors

class DBException(override val message: String, override val cause: Throwable? = null) :
    CustomException(statusCode = 500, message = message, cause = cause)