package dev.urieloalves.domain.user


import dev.urieloalves.studyconnect.domain.shared.AggregateRoot
import dev.urieloalves.studyconnect.domain.shared.validation.ValidationHandler
import dev.urieloalves.studyconnect.domain.user.DiscordUser
import dev.urieloalves.studyconnect.domain.user.UserValidator
import java.util.*

class User private constructor(
    override val id: UUID,
    val email: String,
    val discordUser: DiscordUser
) : AggregateRoot(id) {

    companion object {
        fun newUser(email: String, discordId: String, discordUsername: String): User {
            val discordUser = DiscordUser(
                id = discordId,
                username = discordUsername
            )
            return User(
                id = UUID.randomUUID(),
                email = email,
                discordUser = discordUser
            )
        }
    }

    override fun validate(handler: ValidationHandler) {
        UserValidator(this, handler).validate()
        this.discordUser.validate(handler)
    }
}