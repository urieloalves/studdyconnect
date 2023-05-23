package dev.urieloalves.domain.user.valueobject

class DiscordUser(val id: String, val username: String) {

    init {
        validateId()
        validateUsername()
    }

    private fun validateId() {
        if (id.isEmpty()) {
            throw Exception("Id must not be empty")
        }
        if (id.toLongOrNull() == null) {
            throw Exception("Id '$id' cannot be parsed to long")
        }
    }

    private fun validateUsername() {
        if (username.isEmpty()) {
            throw Exception("Username must not be empty")
        }
    }
}