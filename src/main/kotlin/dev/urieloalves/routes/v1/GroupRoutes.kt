package dev.urieloalves.routes.v1

import dev.urieloalves.data.dao.GroupDaoImpl
import dev.urieloalves.data.dao.GroupUserDaoImpl
import dev.urieloalves.data.dao.UserDaoImpl
import dev.urieloalves.routes.v1.requests.CreateGroupRequest
import dev.urieloalves.routes.v1.responses.GroupResponse
import dev.urieloalves.services.DiscordService
import dev.urieloalves.services.GroupService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.groupRoutes() {

    val groupService = GroupService(
        userDao = UserDaoImpl(),
        groupDao = GroupDaoImpl(),
        groupUserDao = GroupUserDaoImpl(),
        discordService = DiscordService()
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

        get {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asString()
            call.respond(
                groupService.getGroups(userId).map {
                    GroupResponse(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        courseLink = it.courseLink,
                        createdBy = it.createdBy
                    )
                }
            )
        }

        post("/{id}/join") {
            val groupId = call.parameters["id"]!!
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asString()
            groupService.joinGroup(groupId, userId)
            call.response.status(HttpStatusCode.OK)
        }

        post("/{id}/leave") {
            val groupId = call.parameters["id"]!!
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asString()
            groupService.leaveGroup(groupId, userId)
            call.response.status(HttpStatusCode.OK)
        }
    }
}