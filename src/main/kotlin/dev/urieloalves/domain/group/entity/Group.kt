package dev.urieloalves.domain.group.entity

import dev.urieloalves.domain.group.valueobject.DiscordChannel
import java.util.UUID

class Group(
    val id: UUID,
    val name: String,
    val description: String,
    val courseLink: String,
    val createdBy: UUID,
    val discordChannel: DiscordChannel
) {

    init {
        validateName()
        validateDescription()
        validateCourseLink()
    }

    private fun validateName() {
        if (name.isEmpty()) {
            throw Exception("Name must not be empty")
        }
    }

    private fun validateDescription() {
        if (description.isEmpty()) {
            throw Exception("Description must not be empty")
        }
    }

    private fun validateCourseLink() {
        if (courseLink.isEmpty()) {
            throw Exception("CourseLink must not be empty")
        }
    }
}