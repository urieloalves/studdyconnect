package dev.urieloalves.domain.group.factory

import dev.urieloalves.domain.group.entity.Group
import dev.urieloalves.domain.group.valueobject.DiscordChannel
import java.util.UUID

object GroupFactory {

    fun create(
        name: String,
        description: String,
        courseLink: String,
        createdBy: UUID,
        discordChannelId: String
    ): Group {
        return Group(
            id = UUID.randomUUID(),
            name = name,
            description = description,
            courseLink = courseLink,
            createdBy = createdBy,
            discordChannel = DiscordChannel(
                id = discordChannelId
            )
        )
    }
}