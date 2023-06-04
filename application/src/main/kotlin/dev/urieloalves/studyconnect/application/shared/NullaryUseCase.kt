package dev.urieloalves.studyconnect.application.shared


abstract class NullaryUseCase<OUT> {
    abstract fun execute(): OUT
}
