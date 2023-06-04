package dev.urieloalves.studyconnect.domain.user

import dev.urieloalves.studyconnect.domain.shared.ValueObject
import dev.urieloalves.studyconnect.domain.shared.validation.ValidationHandler

class DiscordUser(val id: String, val username: String) : ValueObject() {

    fun validate(handler: ValidationHandler) {
        DiscordUserValidator(this, handler).validate()
    }
}