package dev.urieloalves.domain.group.factory

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GroupFactoryTest {

    @Test
    fun `should create group entity`() {
        val createdBy = UUID.randomUUID()
        val group = GroupFactory.create(
            name = "group-name",
            description = "group-description",
            createdBy = createdBy,
            discordId = "1"
        )

        assertNotNull(group.id)
        assertEquals(group.name, "group-name")
        assertEquals(group.description, "group-description")
        assertEquals(group.createdBy, createdBy)
        assertEquals(group.discordChannel.id, "1")
    }
}