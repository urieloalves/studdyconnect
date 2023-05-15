package dev.urieloalves.domain.group.entity

import dev.urieloalves.domain.group.values.DiscordChannel
import java.util.UUID

class Group (val id: UUID, val name: String, val description: String, val createdBy: UUID, val discordChannel: DiscordChannel) {

    init {
        validateName()
        validateDescription()
    }

    private fun validateName() {
        if(name.isEmpty()) {
            throw Exception("'name' must not be empty")
        }
    }

    private fun validateDescription() {
        if(description.isEmpty()) {
            throw Exception("'description' must not be empty")
        }
    }
}