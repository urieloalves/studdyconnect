package dev.urieloalves.studyconnect.domain.user

import dev.urieloalves.studyconnect.domain.shared.validation.Error
import dev.urieloalves.studyconnect.domain.shared.validation.ValidationHandler
import dev.urieloalves.studyconnect.domain.shared.validation.Validator

class DiscordUserValidator(private val discordUser: DiscordUser, handler: ValidationHandler) : Validator(handler) {

    override fun validate() {
        validateId()
        validateUsername()
    }

    private fun validateId() {
        val id = discordUser.id
        if (id.isEmpty()) {
            validationHandler().append(Error("Id must not be empty"))
        }
        if (id.toLongOrNull() == null) {
            validationHandler().append(Error("Id '$id' cannot be parsed to long"))
        }
    }

    private fun validateUsername() {
        val username = discordUser.username
        if (username.isEmpty()) {
            validationHandler().append(Error("Username must not be empty"))
        }
    }
}