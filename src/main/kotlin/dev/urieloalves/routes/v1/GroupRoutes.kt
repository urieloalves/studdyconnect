package dev.urieloalves.routes.v1

import dev.urieloalves.clients.DiscordClientImpl
import dev.urieloalves.data.dao.GroupDaoImpl
import dev.urieloalves.data.dao.UserDaoImpl
import dev.urieloalves.getUserIdFromToken
import dev.urieloalves.routes.v1.requests.CreateGroupRequest
import dev.urieloalves.routes.v1.responses.GroupResponse
import dev.urieloalves.services.DiscordServiceImpl
import dev.urieloalves.services.GroupServiceImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.groupRoutes() {

    val groupService = GroupServiceImpl(
        userDao = UserDaoImpl(),
        groupDao = GroupDaoImpl(),
        discordService = DiscordServiceImpl(
            discordClient = DiscordClientImpl()
        )
    )

    route("/groups") {
        post {
            groupService.createGroup(
                request = call.receive<CreateGroupRequest>(),
                userId = call.getUserIdFromToken()!!
            )
            call.response.status(HttpStatusCode.Created)
        }

        get {
            val userId = call.getUserIdFromToken()!!
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
            val userId = call.getUserIdFromToken()!!
            groupService.joinGroup(groupId, userId)
            call.response.status(HttpStatusCode.OK)
        }

        post("/{id}/leave") {
            val groupId = call.parameters["id"]!!
            val userId = call.getUserIdFromToken()!!
            groupService.leaveGroup(groupId, userId)
            call.response.status(HttpStatusCode.OK)
        }
    }
}