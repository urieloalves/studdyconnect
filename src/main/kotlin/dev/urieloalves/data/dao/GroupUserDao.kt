package dev.urieloalves.data.dao

import dev.urieloalves.data.tables.GroupUserTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

interface GroupUserDao {
    fun hasUserJoinedGroup(userId: String, groupId: String): Boolean
    fun joinGroup(userId: String, groupId: String)
}

class GroupUserDaoImpl : GroupUserDao {
    override fun hasUserJoinedGroup(userId: String, groupId: String): Boolean {
        val exist = transaction {
            GroupUserTable.select {
                GroupUserTable.userId.eq(userId) and GroupUserTable.groupId.eq(groupId)
            }.limit(1)
                .firstOrNull()
        }
        return exist != null
    }

    override fun joinGroup(userId: String, groupId: String) {
        transaction {
            GroupUserTable.insert {
                it[GroupUserTable.userId] = userId
                it[GroupUserTable.groupId] = groupId
            }
        }
    }
}