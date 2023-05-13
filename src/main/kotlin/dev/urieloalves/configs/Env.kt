package dev.urieloalves.configs

import dev.urieloalves.data.models.errors.CustomException
import io.github.cdimascio.dotenv.Dotenv
import org.slf4j.LoggerFactory

object Env {

    private val logger = LoggerFactory.getLogger("Env")

    var PORT: Int

    var DISCORD_CLIENT_ID: String
    var DISCORD_CLIENT_SECRET: String
    var DISCORD_API_BASE_URL: String
    var DISCORD_MY_REDIRECT_URL: String
    var DISCORD_REDIRECT_URL: String
    var DISCORD_GRANT_TYPE: String
    var DISCORD_BOT_TOKEN: String
    val DISCORD_GUILD_ID: Long
    val DISCORD_ROLE_EVERYONE_ID: String

    var JWT_SECRET: String
    var JWT_EXPIRES_IN_MINUTES: Long

    var DB_HOST: String
    var DB_PORT: String
    var DB_USER: String
    var DB_PASSWORD: String
    var DB_NAME: String

    init {
        logger.info("Loading environment variables")
        val dotenv = Dotenv.configure().ignoreIfMissing().load()

        PORT = dotenv["PORT"]?.toIntOrNull() ?: 8080

        DISCORD_CLIENT_ID = dotenv["DISCORD_CLIENT_ID"] ?: throw notFound("DISCORD_CLIENT_ID")
        DISCORD_CLIENT_SECRET =
            dotenv["DISCORD_CLIENT_SECRET"] ?: throw notFound("DISCORD_CLIENT_SECRET")
        DISCORD_API_BASE_URL =
            dotenv["DISCORD_API_BASE_URL"] ?: throw notFound("DISCORD_API_BASE_URL")
        DISCORD_MY_REDIRECT_URL =
            dotenv["DISCORD_MY_REDIRECT_URL"] ?: throw notFound("DISCORD_MY_REDIRECT_URL")
        DISCORD_REDIRECT_URL =
            dotenv["DISCORD_REDIRECT_URL"] ?: throw notFound("DISCORD_REDIRECT_URL")
        DISCORD_GRANT_TYPE = dotenv["DISCORD_GRANT_TYPE"] ?: throw notFound("DISCORD_GRANT_TYPE")
        DISCORD_BOT_TOKEN = dotenv["DISCORD_BOT_TOKEN"] ?: throw notFound("DISCORD_BOT_TOKEN")
        DISCORD_GUILD_ID = dotenv["DISCORD_GUILD_ID"]?.toLongOrNull() ?: throw notFound("DISCORD_GUILD_ID")
        DISCORD_ROLE_EVERYONE_ID =
            dotenv["DISCORD_ROLE_EVERYONE_ID"] ?: throw notFound("DISCORD_ROLE_EVERYONE_ID")


        JWT_SECRET = dotenv["JWT_SECRET"] ?: throw notFound("JWT_SECRET")
        JWT_EXPIRES_IN_MINUTES =
            dotenv["JWT_EXPIRES_IN_MINUTES"]?.toLongOrNull() ?: throw notFound("JWT_EXPIRES_IN_MINUTES")

        DB_HOST = dotenv["DB_HOST"] ?: throw notFound("DB_HOST")
        DB_PORT = dotenv["DB_PORT"] ?: throw notFound("DB_PORT")
        DB_USER = dotenv["DB_USER"] ?: throw notFound("DB_USER")
        DB_PASSWORD = dotenv["DB_PASSWORD"] ?: throw notFound("DB_PASSWORD")
        DB_NAME = dotenv["DB_NAME"] ?: throw notFound("DB_NAME")
    }

    private fun notFound(variable: String): CustomException {
        return CustomException(
            statusCode = 500,
            message = "Environment variable '$variable' was mot found"
        )
    }
}