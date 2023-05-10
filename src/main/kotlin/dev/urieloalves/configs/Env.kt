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

    init {
        val dotenv = Dotenv.configure().ignoreIfMissing().load()

        this.PORT = dotenv["PORT"]?.toIntOrNull() ?: 8080

        this.DISCORD_CLIENT_ID = dotenv["DISCORD_CLIENT_ID"] ?: throw Error("DISCORD_CLIENT_ID must be provided")
        this.DISCORD_CLIENT_SECRET =
            dotenv["DISCORD_CLIENT_SECRET"] ?: throw Error("DISCORD_CLIENT_SECRET must be provided")
        this.DISCORD_API_BASE_URL =
            dotenv["DISCORD_API_BASE_URL"] ?: throw Error("DISCORD_API_BASE_URL must be provided")
        this.DISCORD_MY_REDIRECT_URL =
            dotenv["DISCORD_MY_REDIRECT_URL"] ?: throw Error("DISCORD_MY_REDIRECT_URL must be provided")
        this.DISCORD_REDIRECT_URL =
            dotenv["DISCORD_REDIRECT_URL"] ?: throw Error("DISCORD_REDIRECT_URL must be provided")
        this.DISCORD_GRANT_TYPE = dotenv["DISCORD_GRANT_TYPE"] ?: throw Error("DISCORD_GRANT_TYPE must be provided")
    }
}