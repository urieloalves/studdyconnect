package dev.urieloalves.domain.user.repository

import dev.urieloalves.domain.user.entity.User
import java.util.UUID

interface UserRepository {
    fun create(entity: User)
    fun findById(id: UUID): User?
    fun findByDiscordId(id: String): User?
}