package dev.urieloalves.infrastructure.discord.error

open class DiscordClientException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message)