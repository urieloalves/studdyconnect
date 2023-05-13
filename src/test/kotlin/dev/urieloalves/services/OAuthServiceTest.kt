package dev.urieloalves.services

import dev.urieloalves.clients.responses.DiscordUserResponse
import dev.urieloalves.data.dao.UserDao
import dev.urieloalves.data.models.User
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OAuthServiceTest {
    private val userDao = mockk<UserDao>()
    private val discordService = mockk<DiscordService>()
    private val jwtService = mockk<JwtService>()
    private val oAuthService = OAuthServiceImpl(
        userDao = userDao,
        discordService = discordService,
        jwtService = jwtService
    )

    private val discordUserResponse = DiscordUserResponse(
        id = "discord-id",
        username = "username",
        email = "username@email.com"
    )

    private val user = User(
        id = "user-id",
        discordId = "discord-id",
        username = "username",
        email = "username@email.com"
    )

    @AfterEach
    fun cleanup() {
        clearAllMocks()
    }

    @Test
    fun `should return a token and user`() {
        coEvery { discordService.getAccessToken(any()) } returns "discord-token"
        coEvery { discordService.getUser("discord-token") } returns discordUserResponse
        every { userDao.getByDiscordId("discord-id") } returns user
        every { jwtService.generateToken("user-id") } returns "token"

        runBlocking {
            val response = oAuthService.handleDiscordOAuthCallback("code")

            assertEquals(response.token, "token")
            assertEquals(response.user.id, "user-id")
            assertEquals(response.user.username, "username")
            assertEquals(response.user.email, "username@email.com")
        }
    }

    @Test
    fun `should create user and join discord server if user is not registered`() {
        coEvery { discordService.getAccessToken(any()) } returns "discord-token"
        coEvery { discordService.getUser("discord-token") } returns discordUserResponse
        every { userDao.getByDiscordId("discord-id") } returns null andThen user
        every {
            userDao.create(
                discordId = "discord-id",
                username = "username",
                email = "username@email.com"
            )
        } just runs
        coEvery {
            discordService.joinServer(
                discordId = "discord-id",
                token = "discord-token"
            )
        } just runs
        every { jwtService.generateToken("user-id") } returns "token"

        runBlocking {
            val response = oAuthService.handleDiscordOAuthCallback("code")

            verify {
                userDao.create(
                    discordId = "discord-id",
                    username = "username",
                    email = "username@email.com"
                )
            }
            coVerify {
                discordService.joinServer(
                    discordId = "discord-id",
                    token = "discord-token"
                )
            }
            verify { userDao.getByDiscordId("discord-id") }
            assertEquals(response.token, "token")
            assertEquals(response.user.id, "user-id")
            assertEquals(response.user.username, "username")
            assertEquals(response.user.email, "username@email.com")
        }
    }
}