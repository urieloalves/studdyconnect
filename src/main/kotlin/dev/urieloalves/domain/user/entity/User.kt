package dev.urieloalves.domain.user.entity

import dev.urieloalves.domain.user.valueobject.DiscordUser
import java.util.UUID

class User(val id: UUID, val email: String, val discordUser: DiscordUser) {

    init {
        validateEmail()
    }

    private fun validateEmail() {
        if (email.trim().isEmpty()) {
            throw Exception("Email must not be empty")
        }
        if (!Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$").matches(email)) {
            throw Exception("Invalid email '$email'")
        }
    }
}