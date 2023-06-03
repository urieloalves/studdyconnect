package dev.urieloalves.studyconnect.infrastructure

import dev.urieloalves.studyconnect.infrastructure.api.module
import dev.urieloalves.studyconnect.infrastructure.shared.DatabaseFactory
import dev.urieloalves.studyconnect.infrastructure.shared.Env
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    DatabaseFactory.init()
    embeddedServer(Netty, port = Env.PORT, module = Application::module).start(wait = true)
}
