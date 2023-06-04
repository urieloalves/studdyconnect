package dev.urieloalves.studyconnect.domain.shared

import dev.urieloalves.studyconnect.domain.shared.validation.ValidationHandler
import java.util.*

abstract class Entity(protected open val id: UUID) {

    abstract fun validate(handler: ValidationHandler)
}