package com.perpetio.database

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.long
import org.ktorm.schema.varchar
import java.time.LocalDateTime

object Users : Table<User>("user") {

    val id = long("id").primaryKey().bindTo { it.id }
    val username = varchar("username").bindTo { it.username }
    val mail = varchar("mail").bindTo { it.mail }
    val password = varchar("password").bindTo { it.password }
    val country = varchar("country").bindTo { it.country }
    val city = varchar("city").bindTo { it.city }
    val createdTime = datetime("createdTime").bindTo { it.createdTime }
    val avatarUrl = varchar("avatarUrl").bindTo { it.avatarUrl }
    val avatarUpdateTime = datetime("avatarUpdateTime").bindTo { it.avatarUpdateTime }
    val avatarId = varchar("avatarId").bindTo { it.avatarId }
}

interface User : Entity<User> {

    companion object : Entity.Factory<User>()

    val id: Long
    val username: String
    val mail: String
    val password: String
    val country: String?
    val city: String?
    val createdTime: LocalDateTime
    val avatarUrl: String?
    val avatarUpdateTime: LocalDateTime?
    val avatarId: String?
}
