package dev.urieloalves.infrastructure.user.repository

import dev.urieloalves.domain.user.entity.User
import dev.urieloalves.domain.user.valueobject.DiscordUser
import dev.urieloalves.infrastructure.user.table.UserTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import dev.urieloalves.domain.user.repository.UserRepository as UserRepositoryInterface

class UserRepository : UserRepositoryInterface {
    
    override fun create(entity: User) {
        transaction {
            UserTable.insert {
                it[id] = entity.id.toString()
                it[discordId] = entity.discordUser.id
                it[username] = entity.discordUser.username
                it[email] = entity.email
            }
        }
    }

    override fun findById(id: UUID): User? {
        return transaction {
            UserTable
                .select { UserTable.id.eq(id.toString()) }
                .limit(1)
                .map {
                    User(
                        id = UUID.fromString(it[UserTable.id]),
                        email = it[UserTable.email],
                        discordUser = DiscordUser(
                            id = it[UserTable.discordId],
                            username = it[UserTable.username]
                        )
                    )
                }
                .firstOrNull()
        }
    }

    override fun findByDiscordId(id: String): User? {
        return transaction {
            UserTable
                .select { UserTable.discordId.eq(id) }
                .limit(1)
                .map {
                    User(
                        id = UUID.fromString(it[UserTable.id]),
                        email = it[UserTable.email],
                        discordUser = DiscordUser(
                            id = it[UserTable.discordId],
                            username = it[UserTable.username]
                        )
                    )
                }
                .firstOrNull()
        }
    }
}