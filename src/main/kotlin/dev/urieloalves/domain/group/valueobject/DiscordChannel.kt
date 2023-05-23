package dev.urieloalves.domain.group.valueobject

class DiscordChannel(val id: String) {

    init {
        validateId()
    }

    private fun validateId() {
        if (id.isEmpty()) {
            throw Exception("Id must not be empty")
        }
        if (id.toLongOrNull() == null) {
            throw Exception("Id '$id' cannot be parsed to long")
        }
    }
}