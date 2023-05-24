package dev.urieloalves.domain.group.valueobject

import dev.urieloalves.domain.shared.error.ValidationException

class DiscordChannel(val id: String) {

    init {
        validateId()
    }

    private fun validateId() {
        if (id.isEmpty()) {
            throw ValidationException("Id must not be empty")
        }
        if (id.toLongOrNull() == null) {
            throw ValidationException("Id '$id' cannot be parsed to long")
        }
    }
}