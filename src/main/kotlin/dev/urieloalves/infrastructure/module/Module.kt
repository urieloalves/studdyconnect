package dev.urieloalves.infrastructure.module

import dev.urieloalves.application.group.CreateGroupUseCase
import dev.urieloalves.application.group.GetGroupsByUserUseCase
import dev.urieloalves.application.group.JoinGroupUseCase
import dev.urieloalves.application.group.LeaveGroupUseCase
import dev.urieloalves.application.group.SearchGroupsUseCase
import dev.urieloalves.application.user.GenerateTokenUseCase
import dev.urieloalves.application.user.HandleOAuthUseCase
import dev.urieloalves.infrastructure.discord.DiscordClient
import dev.urieloalves.infrastructure.discord.DiscordClientImpl
import dev.urieloalves.infrastructure.group.repository.GroupRepository
import dev.urieloalves.infrastructure.user.repository.UserRepository
import dev.urieloalves.domain.group.repository.GroupRepository as GroupRepositoryInterface
import dev.urieloalves.domain.user.repository.UserRepository as UserRepositoryInterface

object Module {

    val discordClient: DiscordClient = DiscordClientImpl()

    val userRepository: UserRepositoryInterface = UserRepository()
    val groupRepository: GroupRepositoryInterface = GroupRepository()

    // user usecases
    val generateTokenUseCase = GenerateTokenUseCase()
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