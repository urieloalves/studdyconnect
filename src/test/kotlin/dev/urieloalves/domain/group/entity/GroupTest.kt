package dev.urieloalves.domain.group.entity

import dev.urieloalves.domain.group.valueobject.DiscordChannel
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GroupTest {

    @Test
    fun `should create group entity`() {
        val id = UUID.randomUUID()
        val createdBy = UUID.randomUUID()
        val group = Group(
            id = id,
            name = "group-name",
            description = "group-description",
            createdBy = createdBy,
            discordChannel = DiscordChannel(
                id = "1"
            )
        )

        assertEquals(group.id, id)
        assertEquals(group.name, "group-name")
        assertEquals(group.description, "group-description")
        assertEquals(group.createdBy, createdBy)
        assertEquals(group.discordChannel.id, "1")
    }

    @Test
    fun `should throw if name is empty`() {
        assertThrows<Exception> {
            Group(
                id = UUID.randomUUID(),
                name = "",
                description = "group-description",
                createdBy = UUID.randomUUID(),
                discordChannel = DiscordChannel(
                    id = "1"
                )
            )
        }
    }

    @Test
    fun `should throw if description is empty`() {
        assertThrows<Exception> {
            Group(
                id = UUID.randomUUID(),
                name = "group-name",
                description = "",
                createdBy = UUID.randomUUID(),
                discordChannel = DiscordChannel(
                    id = "1"
                )
            )
        }
    }
}