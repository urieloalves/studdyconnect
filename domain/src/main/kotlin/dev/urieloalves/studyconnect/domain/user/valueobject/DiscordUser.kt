package dev.urieloalves.domain.user.valueobject

import dev.urieloalves.domain.shared.error.ValidationException

class DiscordUser(val id: String, val username: String) {

    init {
        validateId()
        validateUsername()
    }

    private fun validateId() {
        if (id.isEmpty()) {
            throw ValidationException("Id must not be empty")
        }
        if (id.toLongOrNull() == null) {
            throw ValidationException("Id '$id' cannot be parsed to long")
        }
    }

    private fun validateUsername() {
        if (username.isEmpty()) {
            throw ValidationException("Username must not be empty")
        }
    }
}