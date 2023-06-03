package dev.urieloalves.domain.domain.user.factory

import dev.urieloalves.domain.user.factory.UserFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserFactoryTest {

    @Test
    fun `should create user entity`() {
        val user = UserFactory.create(
            email = "user@email.com",
            discordId = "1",
            discordUsername = "discord-username"
        )

        assertNotNull(user.id)
        assertEquals(user.email, "user@email.com")
        assertEquals(user.discordUser.id, "1")
        assertEquals(user.discordUser.username, "discord-username")
    }
}