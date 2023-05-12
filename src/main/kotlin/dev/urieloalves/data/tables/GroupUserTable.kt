package dev.urieloalves.data.tables

import org.jetbrains.exposed.sql.Table

object GroupUserTable : Table("group_user") {
    val userId = text("user_id")
    val groupId = text("group_id")
}
