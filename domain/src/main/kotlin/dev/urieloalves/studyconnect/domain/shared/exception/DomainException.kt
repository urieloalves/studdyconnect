package dev.urieloalves.studyconnect.domain.shared.exception

import dev.urieloalves.studyconnect.domain.shared.validation.Error


class DomainException private constructor(message: String, private val errors: List<Error>) :
    NoStackTraceException(message) {

    companion object {
        fun with(error: Error): DomainException {
            return DomainException(error.message, listOf(error))
        }

        fun with(errors: List<Error>): DomainException {
            return DomainException("", errors)
        }
    }

    fun getErrors(): List<Error> {
        return errors
    }
}
