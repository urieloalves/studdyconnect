package dev.urieloalves.domain.user.entity

import dev.urieloalves.domain.user.entity.User
import dev.urieloalves.domain.user.value.DiscordUser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTest {

    @Test
    fun `should create user entity`() {
        val id  = UUID.randomUUID()
        val user = User(
            id = id,
            email = "user@email.com",
            discordUser = DiscordUser(
                id="1",
                username = "discord-username"
            )
        )

        assertEquals(user.id, id)
        assertEquals(user.email, "user@email.com")
        assertEquals(user.discordUser.id, "1")
        assertEquals(user.discordUser.username, "discord-username")
    }

    @Test
    fun `should throw if email is empty`() {
        assertThrows<Exception> {
            User(
                id = UUID.randomUUID(),
                email = "",
                discordUser = DiscordUser(
                    id="1",
                    username = "discord-username"
                )
            )
        }
    }

    @Test
    fun `should throw if email is invalid`() {
        assertThrows<Exception> {
            User(
                id = UUID.randomUUID(),
                email = "incorrect-email",
                discordUser = DiscordUser(
                    id="1",
                    username = "discord-username"
                )
            )
        }
    }
}