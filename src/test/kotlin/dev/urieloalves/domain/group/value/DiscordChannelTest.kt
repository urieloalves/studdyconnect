package dev.urieloalves.domain.group.value

import dev.urieloalves.domain.group.values.DiscordChannel
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DiscordChannelTest {

    @Test
    fun `should create DiscordChannel value-object`() {
        val discordChannel = DiscordChannel(
            id = "1"
        )
        assertEquals(discordChannel.id, "1")
    }

    @Test
    fun `should throw if id is empty`() {
        assertThrows<Exception> {
            DiscordChannel(
                id = ""
            )
        }
    }

    @Test
    fun `should throw if id cannot be parsed to long`() {
        assertThrows<Exception> {
            DiscordChannel(
                id = "not-long"
            )
        }
    }
}