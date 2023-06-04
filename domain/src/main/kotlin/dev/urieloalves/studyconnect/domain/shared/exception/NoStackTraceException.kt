package dev.urieloalves.studyconnect.domain.shared.exception


open class NoStackTraceException @JvmOverloads constructor(message: String?, cause: Throwable? = null) :
    RuntimeException(message, cause, true, false)
