package dev.urieloalves.studyconnect.domain.shared.validation

import dev.urieloalves.studyconnect.domain.shared.exception.DomainException

class Notification(private val errors: List<Error> = emptyList()) : ValidationHandler {

    fun create(): Notification {
        return Notification(emptyList())
    }

    fun create(error: Error): Notification {
        return Notification().append(error)
    }

    fun create(t: Throwable): Notification {
        return create(Error(t.message))
    }

    override fun append(error: Error): Notification {
        errors.plus(error)
        return this
    }

    override fun append(handler: ValidationHandler): Notification {
        errors.plus(handler.getErrors())
        return this
    }

    override fun validate(validation: ValidationHandler.Validation): Notification {
        try {
            validation.validate()
        } catch (e: DomainException) {
            errors.plus(e.getErrors())
        } catch (t: Throwable) {
            errors.plus(Error(t.message ?: ""))
        }
        return this
    }

    override fun getErrors(): List<Error> {
        return errors
    }
}