package com.perpetio.repository.postLike

import com.perpetio.database.entity.PostUserLikes
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf

class PostLikeRepositoryImpl(private val db: Database) : PostLikeRepository {

    override fun isAlreadyLiked(userId: Long, postId: Long): Boolean {
        return db.sequenceOf(PostUserLikes).firstOrNull { it.postId eq postId and (it.userId eq userId) } != null
    }

    override fun addLike(userId: Long, postId: Long) {
        db.insert(PostUserLikes) {
            set(it.postId, postId)
            set(it.userId, userId)
        }
    }

    override fun removeLike(userId: Long, postId: Long) {
        db.delete(PostUserLikes) { it.userId eq userId and (it.postId eq postId) }
    }
}