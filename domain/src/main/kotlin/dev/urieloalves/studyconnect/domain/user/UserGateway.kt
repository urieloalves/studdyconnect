package dev.urieloalves.studyconnect.domain.user

import dev.urieloalves.domain.user.User
import java.util.*

interface UserGateway {
    fun create(user: User)
    fun findById(id: UUID): User?
    fun findByDiscordId(id: String): User?
}