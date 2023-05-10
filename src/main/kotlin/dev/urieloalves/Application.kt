package dev.urieloalves

import io.ktor.server.application.*
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.github.cdimascio.dotenv.Dotenv

fun main(args: Array<String>): Unit {
    val dotenv = Dotenv.configure().ignoreIfMissing().load()
    val port = dotenv["PORT"]?.toIntOrNull() ?: 8080
    embeddedServer(Netty, port = port, module = Application::module).start(wait = true)
}


@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(CORS) {
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.Authorization)
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
