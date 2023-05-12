package dev.urieloalves.data.tables

import org.jetbrains.exposed.sql.Table

object GroupUserTable : Table("groups_users") {
    val userId = text("user_id")
    val groupId = text("group_id")
}
