package dev.urieloalves.infrastructure.httpserver.error

open class CustomException(
    val statusCode: Int,
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message) {

    override fun toString(): String {
        return "CustomException(statusCode=$statusCode, message='$message', cause='$cause')"
    }
}