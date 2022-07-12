package com.perpetio.database.entity

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.long

object PostUserLikes : Table<PostUserLike>("post_user_like") {

    val id = long("id").primaryKey().bindTo { it.id }
    val userId = long("userId").bindTo { it.userId }
    val postId = long("postId").references(Posts) { it.post }
}

interface PostUserLike : Entity<PostUserLike> {

    companion object : Entity.Factory<RoomToUser>()

    val id: Long
    val userId: Long
    val post: Post
}