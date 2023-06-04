package dev.urieloalves.studyconnect.domain.user

import dev.urieloalves.domain.shared.error.ValidationException
import dev.urieloalves.domain.user.User
import dev.urieloalves.studyconnect.domain.shared.validation.ValidationHandler
import dev.urieloalves.studyconnect.domain.shared.validation.Validator

class UserValidator(private val user: User, handler: ValidationHandler) : Validator(handler) {

    override fun validate() {
        validateEmail()
    }

    private fun validateEmail() {
        val email = user.email
        if (email.trim().isEmpty()) {
            throw ValidationException("Email must not be empty")
        }
        if (!Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$").matches(email)) {
            throw ValidationException("Invalid email '$email'")
        }
    }
}