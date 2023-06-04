package dev.urieloalves.studyconnect.domain.shared.validation

interface ValidationHandler {
    fun append(error: Error): ValidationHandler

    fun append(handler: ValidationHandler): ValidationHandler

    fun validate(validation: Validation): ValidationHandler

    fun getErrors(): List<Error>

    fun hasError(): Boolean {
        return getErrors().isNotEmpty()
    }

    interface Validation {
        fun validate()
    }
}