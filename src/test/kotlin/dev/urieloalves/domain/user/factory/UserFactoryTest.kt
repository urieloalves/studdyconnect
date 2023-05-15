package dev.urieloalves.domain.user.factory

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserFactoryTest {

    @Test
    fun `should create user entity`() {
        val id  = UUID.randomUUID()
        val user = UserFactory.create(
            id = id,
            email = "user@email.com",
            discordId="1",
            discordUsername = "discord-username"
        )

        assertEquals(user.id, id)
        assertEquals(user.email, "user@email.com")
        assertEquals(user.discordUser.id, "1")
        assertEquals(user.discordUser.username, "discord-username")
    }
}