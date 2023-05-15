package dev.urieloalves.domain.user.value

import dev.urieloalves.domain.user.entity.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DiscordUserTest {

    @Test
    fun `should create discord user value-object`() {
        val discordUser = DiscordUser(
            id="1",
            username = "discord-username"
        )

        assertEquals(discordUser.id, "1")
        assertEquals(discordUser.username, "discord-username")
    }

    @Test
    fun `should throw if id is empty`() {
        assertThrows<Exception> {
            DiscordUser(
                id="",
                username = "discord-username"
            )
        }
    }

    @Test
    fun `should throw if id cannot be parsed to long`() {
        assertThrows<Exception> {
            DiscordUser(
                id="not-long",
                username = "discord-username"
            )
        }
    }

    @Test
    fun `should throw if username is empty`() {
        assertThrows<Exception> {
            DiscordUser(
                id="1",
                username = ""
            )
        }
    }
}