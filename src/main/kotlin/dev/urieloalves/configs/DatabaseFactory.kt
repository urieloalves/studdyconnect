package dev.urieloalves.configs

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun init() {
        val url = "jdbc:postgresql://${Env.DB_HOST}:${Env.DB_PORT}/${Env.DB_NAME}"
        val driver = "org.postgresql.Driver"
        val user = Env.DB_USER
        val password = Env.DB_PASSWORD

        val flyway = Flyway.configure()
            .dataSource(url, user, password)
            .load()
        flyway.migrate()

        Database.connect(url, driver, user, password)
    }
}