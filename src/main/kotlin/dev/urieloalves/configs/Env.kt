package dev.urieloalves.configs

import io.github.cdimascio.dotenv.Dotenv

object Env {

    var PORT: Int

    var DISCORD_CLIENT_ID: String
    var DISCORD_CLIENT_SECRET: String
    var DISCORD_API_BASE_URL: String
    var DISCORD_MY_REDIRECT_URL: String
    var DISCORD_REDIRECT_URL: String
    var DISCORD_GRANT_TYPE: String

    var JWT_SECRET: String
    var JWT_EXPIRES_IN_MINUTES: Long

    var DB_DRIVER: String
    var DB_HOST: String
    var DB_PORT: String
    var DB_USER: String
    var DB_PASSWORD: String
    var DB_NAME: String

    init {
        val dotenv = Dotenv.configure().ignoreIfMissing().load()

        PORT = dotenv["PORT"]?.toIntOrNull() ?: 8080

        DISCORD_CLIENT_ID = dotenv["DISCORD_CLIENT_ID"] ?: throw Error("DISCORD_CLIENT_ID must be provided")
        DISCORD_CLIENT_SECRET =
            dotenv["DISCORD_CLIENT_SECRET"] ?: throw Error("DISCORD_CLIENT_SECRET must be provided")
        DISCORD_API_BASE_URL =
            dotenv["DISCORD_API_BASE_URL"] ?: throw Error("DISCORD_API_BASE_URL must be provided")
        DISCORD_MY_REDIRECT_URL =
            dotenv["DISCORD_MY_REDIRECT_URL"] ?: throw Error("DISCORD_MY_REDIRECT_URL must be provided")
        DISCORD_REDIRECT_URL =
            dotenv["DISCORD_REDIRECT_URL"] ?: throw Error("DISCORD_REDIRECT_URL must be provided")
        DISCORD_GRANT_TYPE = dotenv["DISCORD_GRANT_TYPE"] ?: throw Error("DISCORD_GRANT_TYPE must be provided")

        JWT_SECRET = dotenv["JWT_SECRET"] ?: throw Error("JWT_SECRET must be provided")
        JWT_EXPIRES_IN_MINUTES =
            dotenv["JWT_EXPIRES_IN_MINUTES"]?.toLongOrNull() ?: throw Error("JWT_EXPIRES_IN_MINUTES must be provided")

        DB_DRIVER = dotenv["DB_DRIVER"] ?: throw Error("DB_DRIVER must be provided")
        DB_HOST = dotenv["DB_HOST"] ?: throw Error("DB_HOST must be provided")
        DB_PORT = dotenv["DB_PORT"] ?: throw Error("DB_PORT must be provided")
        DB_USER = dotenv["DB_USER"] ?: throw Error("DB_USER must be provided")
        DB_PASSWORD = dotenv["DB_PASSWORD"] ?: throw Error("DB_PASSWORD must be provided")
        DB_NAME = dotenv["DB_NAME"] ?: throw Error("DB_NAME must be provided")
    }
}