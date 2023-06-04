package dev.urieloalves.studyconnect.application.shared

abstract class UseCase<IN, OUT> {
    abstract fun execute(input: IN): OUT
}
