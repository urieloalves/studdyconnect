package dev.urieloalves.domain.user.value

class DiscordUser(val id: String, val username: String) {

    init {
        validateId()
        validateUsername()
    }

    private fun validateId() {
        if (id.isEmpty()) {
            throw Exception("'id' must not be empty")
        }
        if(id.toLongOrNull() == null) {
            throw Exception("'id' cannot be parsed to long")
        }
    }

    private fun validateUsername() {
        if (username.isEmpty()) {
            throw Exception("'username' must not be empty")
        }
    }
}