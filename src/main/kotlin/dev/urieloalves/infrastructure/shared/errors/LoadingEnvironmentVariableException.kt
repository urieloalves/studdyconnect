package dev.urieloalves.infrastructure.shared.errors

class LoadingEnvironmentVariableException(override val message: String, override val cause: Throwable? = null) :
    CustomException(statusCode = 500, message = message, cause = cause)