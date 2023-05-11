package dev.urieloalves.configs

import dev.urieloalves.data.tables.DiscordUsersTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val database = create()
        transaction(database) {
            SchemaUtils.create(DiscordUsersTable)
        }
    }

    private fun create(): Database {
        val url = "jdbc:${Env.DB_DRIVER}://${Env.DB_HOST}:${Env.DB_PORT}/${Env.DB_NAME}"
        val driver = "org.postgresql.Driver"
        val user = Env.DB_USER
        val password = Env.DB_PASSWORD

        return Database.connect(url, driver, user, password)
    }
}