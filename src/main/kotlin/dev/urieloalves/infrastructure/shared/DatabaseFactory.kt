package dev.urieloalves.infrastructure.shared

import dev.urieloalves.infrastructure.shared.errors.DBException
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory

object DatabaseFactory {

    private val logger = LoggerFactory.getLogger("DatabaseFactory")

    fun init() {
        try {
            val url = "jdbc:postgresql://${Env.DB_HOST}:${Env.DB_PORT}/${Env.DB_NAME}"
            val driver = "org.postgresql.Driver"
            val user = Env.DB_USER
            val password = Env.DB_PASSWORD

            val flyway = Flyway.configure()
                .dataSource(url, user, password)
                .load()
            flyway.migrate()

            Database.connect(url, driver, user, password)
        } catch (e: Exception) {
            val msg = "An error occurred when connecting or migrating DB"
            logger.error(msg, e)
            throw DBException(msg, e)
        }
    }
}