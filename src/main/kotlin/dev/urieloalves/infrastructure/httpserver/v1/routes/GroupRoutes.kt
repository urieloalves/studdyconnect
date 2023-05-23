package dev.urieloalves.infrastructure.httpserver.v1.routes

import io.ktor.server.routing.Route

fun Route.groupRoutes() {

//    val groupService = GroupServiceImpl(
//        userDao = UserDaoImpl(),
//        groupDao = GroupDaoImpl(),
//        discordService = DiscordServiceImpl(
//            discordClient = DiscordClientImpl()
//        )
//    )
//
//    route("/groups") {
//        post {
//            groupService.createGroup(
//                request = call.receive<CreateGroupRequest>(),
//                userId = call.getUserIdFromToken()
//            )
//            call.response.status(HttpStatusCode.Created)
//        }
//
//        get {
//            val userId = call.getUserIdFromToken()
//            call.respond(
//                groupService.getGroups(userId).map {
//                    GroupResponse(
//                        id = it.id,
//                        name = it.name,
//                        description = it.description,
//                        courseLink = it.courseLink,
//                        createdBy = it.createdBy
//                    )
//                }
//            )
//        }
//
//        get("/search") {
//            val text =
//                call.request.queryParameters["text"] ?: throw ClientException("Query parameter 'text' must be provided")
//            call.respond(
//                groupService.searchGroups(text).map {
//                    GroupResponse(
//                        id = it.id,
//                        name = it.name,
//                        description = it.description,
//                        courseLink = it.courseLink,
//                        createdBy = it.createdBy
//                    )
//                }
//            )
//        }
//
//        post("/{id}/join") {
//            val groupId = call.parameters["id"] ?: throw ServerException("Could not extract path parameter 'id'")
//            val userId = call.getUserIdFromToken()
//            groupService.joinGroup(groupId, userId)
//            call.response.status(HttpStatusCode.OK)
//        }
//
//        post("/{id}/leave") {
//            val groupId = call.parameters["id"] ?: throw ServerException("Could not extract path parameter 'id'")
//            val userId = call.getUserIdFromToken()
//            groupService.leaveGroup(groupId, userId)
//            call.response.status(HttpStatusCode.OK)
//        }
//    }
}