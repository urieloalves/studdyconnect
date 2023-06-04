package dev.urieloalves.studyconnect.application.shared

abstract class UnitUseCase<IN> {
    abstract fun execute(input: IN)
}
