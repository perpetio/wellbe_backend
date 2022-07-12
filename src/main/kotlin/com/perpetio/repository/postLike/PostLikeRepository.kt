package com.perpetio.repository.postLike

interface PostLikeRepository {

    fun isAlreadyLiked(userId: Long, postId: Long): Boolean

    fun addLike(userId: Long, postId: Long)

    fun removeLike(userId: Long, postId: Long)
}