package dev.urieloalves.domain.user.factory

import dev.urieloalves.domain.user.entity.User
import dev.urieloalves.domain.user.value.DiscordUser
import java.util.UUID

object UserFactory {

    fun create(id: UUID, email: String, discordId: String, discordUsername: String): User {
        return User(
            id = id,
            email = email,
            discordUser = DiscordUser(
                id=discordId,
                username = discordUsername
            )
        )
    }
}