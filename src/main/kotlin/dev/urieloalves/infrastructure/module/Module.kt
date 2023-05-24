package dev.urieloalves.infrastructure.module

import dev.urieloalves.infrastructure.discord.DiscordClient
import dev.urieloalves.infrastructure.discord.DiscordClientImpl
import dev.urieloalves.infrastructure.group.repository.GroupRepository
import dev.urieloalves.infrastructure.user.repository.UserRepository
import dev.urieloalves.usecase.group.CreateGroupUseCase
import dev.urieloalves.usecase.group.GetGroupsByUserUseCase
import dev.urieloalves.usecase.user.GenerateTokenUseCase
import dev.urieloalves.usecase.user.HandleOAuthUseCase
import dev.urieloalves.usecase.user.JoinGroupUseCase
import dev.urieloalves.usecase.user.LeaveGroupUseCase
import dev.urieloalves.usecase.user.SearchGroupsUseCase
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