package dev.urieloalves.domain.group.factory

import dev.urieloalves.domain.group.entity.Group
import dev.urieloalves.domain.group.values.DiscordChannel
import java.util.UUID

object GroupFactory {

    fun create(name: String, description: String, createdBy: UUID, discordId: String): Group {
        return Group(
            id = UUID.randomUUID(),
            name = name,
            description= description,
            createdBy = createdBy,
            discordChannel = DiscordChannel(
                id= discordId
            )
        )
    }
}