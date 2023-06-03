package dev.urieloalves.studyconnect.infrastructure.module

import dev.urieloalves.application.group.*
import dev.urieloalves.application.user.GenerateTokenUseCase
import dev.urieloalves.application.user.HandleOAuthUseCase
import dev.urieloalves.studyconnect.application.discord.DiscordClient
import dev.urieloalves.studyconnect.infrastructure.discord.DiscordClientImpl
import dev.urieloalves.studyconnect.infrastructure.group.repository.GroupRepository
import dev.urieloalves.studyconnect.infrastructure.shared.Env
import dev.urieloalves.studyconnect.infrastructure.user.repository.UserRepository
import dev.urieloalves.domain.group.repository.GroupRepository as GroupRepositoryInterface
import dev.urieloalves.domain.user.repository.UserRepository as UserRepositoryInterface

object Module {

    val discordClient: DiscordClient = DiscordClientImpl()

    val userRepository: UserRepositoryInterface = UserRepository()
    val groupRepository: GroupRepositoryInterface = GroupRepository()

    // user usecases
    val generateTokenUseCase = GenerateTokenUseCase(
        jwtSecret = Env.JWT_SECRET,
        expiresInMinutes = Env.JWT_EXPIRES_IN_MINUTES
    )
    val handleOauthUseCase = HandleOAuthUseCase(
        discordClient = discordClient,
        userRepository = userRepository,
        generateTokenUseCase = generateTokenUseCase
    )
    val joinGroupUseCase = JoinGroupUseCase(
        userRepository = userRepository,
        groupRepository = groupRepository,
        discordClient = discordClient
    )
    val leaveGroupUseCase = LeaveGroupUseCase(
        userRepository = userRepository,
        groupRepository = groupRepository,
        discordClient = discordClient
    )
    val searchGroupsUseCase = SearchGroupsUseCase(
        groupRepository = groupRepository
    )

    // group usecases
    val createGroupsUseCase = CreateGroupUseCase(
        userRepository = userRepository,
        groupRepository = groupRepository,
        discordClient = discordClient
    )
    val getGroupsByUserUseCase = GetGroupsByUserUseCase(
        groupRepository = groupRepository
    )
}