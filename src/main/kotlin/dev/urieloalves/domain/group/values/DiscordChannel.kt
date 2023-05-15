package dev.urieloalves.domain.group.values

class DiscordChannel(val id: String) {

    init {
        validateId()
    }

    private fun validateId() {
        if(id.isEmpty()) {
            throw Exception("'id' must not be empty")
        }
    }
}