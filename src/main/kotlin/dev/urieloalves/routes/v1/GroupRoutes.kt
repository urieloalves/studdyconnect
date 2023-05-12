package dev.urieloalves.routes.v1

import dev.urieloalves.data.dao.DiscordChannelDaoImpl
import dev.urieloalves.data.dao.GroupDaoImpl
import dev.urieloalves.routes.v1.requests.CreateGroupRequest
import dev.urieloalves.services.GroupService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.groupRoutes() {

    val groupService = GroupService(
        groupDao = GroupDaoImpl(),
        discordChannelDao = DiscordChannelDaoImpl()
    )

    route("/groups") {
        post {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asString()
            groupService.createGroup(
                request = call.receive<CreateGroupRequest>(),
                userId = userId
            )
            call.response.status(HttpStatusCode.Created)
        }
    }
}