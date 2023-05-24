package dev.urieloalves.infrastructure.httpserver.route.v1

import dev.urieloalves.infrastructure.httpserver.getUserIdFromToken
import dev.urieloalves.infrastructure.httpserver.route.v1.request.CreateGroupRequest
import dev.urieloalves.infrastructure.httpserver.route.v1.response.GroupResponse
import dev.urieloalves.infrastructure.module.Module
import dev.urieloalves.infrastructure.shared.errors.ClientException
import dev.urieloalves.infrastructure.shared.errors.ServerException
import dev.urieloalves.usecase.group.dto.InputCreateGroupUseCaseDto
import dev.urieloalves.usecase.group.dto.InputGetGroupsByUserUseCaseDto
import dev.urieloalves.usecase.user.dto.InputJoinGroupUseCaseDto
import dev.urieloalves.usecase.user.dto.InputLeaveGroupUseCaseDto
import dev.urieloalves.usecase.user.dto.InputSearchGroupUseCaseDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import java.util.UUID

fun Route.groupRoutes() {

    route("/groups") {
        post {
            val request = call.receive<CreateGroupRequest>()
            Module.createGroupsUseCase.execute(
                InputCreateGroupUseCaseDto(
                    groupName = request.name,
                    groupDescription = request.description,
                    courseLink = request.courseLink,
                    userId = call.getUserIdFromToken()
                )
            )
            call.response.status(HttpStatusCode.Created)
        }

        get {
            val output = Module.getGroupsByUserUseCase.execute(
                InputGetGroupsByUserUseCaseDto(
                    userId = call.getUserIdFromToken()
                )
            )
            call.respond(
                output.map {
                    GroupResponse(
                        id = it.id.toString(),
                        name = it.name,
                        description = it.description,
                        courseLink = it.courseLink,
                        createdBy = it.createdBy.toString()
                    )
                }
            )
        }

        get("/search") {
            val text =
                call.request.queryParameters["text"] ?: throw ClientException("Query parameter 'text' must be provided")
            val output = Module.searchGroupsUseCase.execute(
                InputSearchGroupUseCaseDto(
                    text = text
                )
            )
            call.respond(
                output.map {
                    GroupResponse(
                        id = it.id.toString(),
                        name = it.name,
                        description = it.description,
                        courseLink = it.courseLink,
                        createdBy = it.createdBy.toString()
                    )
                }
            )
        }

        post("/{id}/join") {
            val groupId = call.parameters["id"] ?: throw ServerException("Could not extract path parameter 'id'")
            Module.joinGroupUseCase.execute(
                InputJoinGroupUseCaseDto(
                    groupId = UUID.fromString(groupId),
                    userId = call.getUserIdFromToken()
                )
            )
            call.response.status(HttpStatusCode.OK)
        }

        post("/{id}/leave") {
            val groupId = call.parameters["id"] ?: throw ServerException("Could not extract path parameter 'id'")
            Module.leaveGroupUseCase.execute(
                InputLeaveGroupUseCaseDto(
                    groupId = UUID.fromString(groupId),
                    userId = call.getUserIdFromToken()
                )
            )
            call.response.status(HttpStatusCode.OK)
        }
    }
}