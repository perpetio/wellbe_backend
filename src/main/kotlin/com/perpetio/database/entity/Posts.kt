package com.perpetio.database.entity

import com.perpetio.database.User
import com.perpetio.database.Users
import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDateTime

object Posts : Table<Post>("post") {

    val id = long("id").primaryKey().bindTo { it.id }
    val userId = long("userId").references(Users) { it.user }
    val title = varchar("title").bindTo { it.title }
    val text = varchar("text").bindTo { it.text }
    val visible = boolean("visible").bindTo { it.visible }
    val tag = varchar("tag").bindTo { it.tag }
    val createdTime = datetime("createdTime").bindTo { it.createdTime }
    val backColor = varchar("backColor").bindTo { it.backColor }
    val likes = long("likes").bindTo { it.likes }
    val sign = int("sign").bindTo { it.sign }
    val signColor = varchar("signColor").bindTo { it.signColor }
}

interface Post : Entity<Post> {

    companion object : Entity.Factory<Post>()

    val id: Long
    val user: User
    val title: String
    val text: String
    val visible: Boolean
    val tag: String?
    val createdTime: LocalDateTime
    val backColor: String
    val likes: Long
    val sign: Int
    val signColor: String?
}