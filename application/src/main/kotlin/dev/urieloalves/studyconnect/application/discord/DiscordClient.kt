package dev.urieloalves.studyconnect.application.discord

import dev.urieloalves.infrastructure.discord.dto.*

interface DiscordClient {
    suspend fun getAccessToken(input: InputGetAccessTokenDto): OutputGetAccessTokenDto
    suspend fun getUser(input: InputGetUserDto): OutputGetUserDto
    suspend fun joinServer(input: InputJoinServerDto)
    suspend fun createChannel(input: InputCreateChannelDto): OutputCreateChannelDto
    suspend fun joinChannel(input: InputJoinChannelDto)
    suspend fun leaveChannel(input: InputLeaveChannelDto)
}