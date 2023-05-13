package dev.urieloalves.services

import dev.urieloalves.clients.DiscordClient
import dev.urieloalves.clients.responses.GetUserInfoResponse
import dev.urieloalves.data.dao.UserDao
import dev.urieloalves.data.models.User
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OAuthServiceTest {
    private val userDao = mockk<UserDao>()
    private val discordService = mockk<DiscordService>()
    private val discordClient = mockk<DiscordClient>()
    private val jwtService = mockk<JwtService>()
    private val oAuthService = OAuthService(
        userDao = userDao,
        discordService = discordService,
        discordClient = discordClient,
        jwtService = jwtService
    )

    @AfterEach
    fun cleanup() {
        println(1)
        clearAllMocks()
    }

    @Test
    fun `should return a token and user`() {
        coEvery { discordClient.getAccessToken(any()) } returns "token"
        coEvery { discordClient.getUser(any()) } returns GetUserInfoResponse(
            id = "discord-id",
            username = "username",
            email = "username@email.com"
        )
        every { userDao.getByDiscordId(any()) } returns User(
            id = "id",
            discordId = "discord-id",
            username = "username",
            email = "username@email.com"
        )
        every { jwtService.generateToken(any()) } returns "token"

        runBlocking {
            val response = oAuthService.handleDiscordOAuthCallback("code")

            assertEquals(response.token, "token")
            assertEquals(response.user.id, "id")
            assertEquals(response.user.username, "username")
            assertEquals(response.user.email, "username@email.com")
        }
    }
}