package dev.urieloalves.infrastructure

import dev.urieloalves.infrastructure.api.module
import dev.urieloalves.infrastructure.shared.DatabaseFactory
import dev.urieloalves.infrastructure.shared.Env
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    DatabaseFactory.init()
    embeddedServer(Netty, port = Env.PORT, module = Application::module).start(wait = true)
}
