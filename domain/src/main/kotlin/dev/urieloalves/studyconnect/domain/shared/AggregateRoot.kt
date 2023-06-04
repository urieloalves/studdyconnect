package dev.urieloalves.studyconnect.domain.shared

import java.util.*

abstract class AggregateRoot protected constructor(id: UUID) :
    Entity(id)

