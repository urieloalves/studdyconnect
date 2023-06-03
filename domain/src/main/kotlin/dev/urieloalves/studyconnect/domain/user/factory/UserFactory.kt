package dev.urieloalves.domain.user.factory

import dev.urieloalves.domain.user.entity.User
import dev.urieloalves.domain.user.valueobject.DiscordUser
import java.util.UUID

object UserFactory {

    fun create(email: String, discordId: String, discordUsername: String): User {
        return User(
            id = UUID.randomUUID(),
            email = email,
            discordUser = DiscordUser(
                id = discordId,
                username = discordUsername
            )
        )
    }
}