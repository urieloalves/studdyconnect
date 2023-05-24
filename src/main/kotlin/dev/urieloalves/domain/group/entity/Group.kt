package dev.urieloalves.domain.group.entity

import dev.urieloalves.domain.group.valueobject.DiscordChannel
import dev.urieloalves.domain.shared.error.ValidationException
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
            throw ValidationException("Name must not be empty")
        }
    }

    private fun validateDescription() {
        if (description.isEmpty()) {
            throw ValidationException("Description must not be empty")
        }
    }

    private fun validateCourseLink() {
        if (courseLink.isEmpty()) {
            throw ValidationException("CourseLink must not be empty")
        }
    }
}