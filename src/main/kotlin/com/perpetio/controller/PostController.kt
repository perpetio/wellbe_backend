package com.perpetio.controller

import com.perpetio.dto.post.CreateEditPostModel
import com.perpetio.dto.post.PostModel
import com.perpetio.exception.PostAlreadyLikedException
import com.perpetio.repository.post.PostRepository
import com.perpetio.repository.postLike.PostLikeRepository
import com.perpetio.util.SearchFilter
import com.perpetio.util.pagination.Pagination
import com.perpetio.util.pagination.PaginationResult

class PostController(private val postRepo: PostRepository, private val postLikeRepo: PostLikeRepository) {

    fun createPost(userId: Long, post: CreateEditPostModel): CreateEditPostModel {
        val postId = postRepo.createPost(userId, post)
        return post.apply { this.id = postId }
    }

    fun editPost(userId: Long, post: CreateEditPostModel) {
        postRepo.editPost(userId, post)
    }

    fun removePost(userId: Long, postId: Long) {
        postRepo.removePost(userId = userId, postId)
    }

    fun selectUserPosts(userId: Long, pagination: Pagination, search: SearchFilter?): PaginationResult<PostModel> {
        return postRepo.selectUserPosts(userId, pagination, search)
    }

    fun selectFeedPosts(userId: Long, pagination: Pagination, search: SearchFilter?): PaginationResult<PostModel> {
        return postRepo.selectFeedPosts(userId, pagination, search)
    }

    fun selectUserFavouritePosts(
        userId: Long,
        pagination: Pagination,
        search: SearchFilter?
    ): PaginationResult<PostModel> {
        return postRepo.selectUserFavouritePosts(userId, pagination, search)
    }

    fun selectPopularPosts(userId: Long, pagination: Pagination, search: SearchFilter?): PaginationResult<PostModel> {
        return postRepo.selectPopularPosts(userId, pagination, search)
    }

    fun addLike(userId: Long, postId: Long) {
        if (!postLikeRepo.isAlreadyLiked(userId, postId)) {
            postLikeRepo.addLike(userId, postId)
            postRepo.incrementLikeCounter(postId)
        } else throw PostAlreadyLikedException()
    }

    fun removeLike(userId: Long, postId: Long) {
        if (postLikeRepo.isAlreadyLiked(userId, postId)) {
            postLikeRepo.removeLike(userId, postId)
            postRepo.decrementLikeCounter(postId)
        } else throw PostAlreadyLikedException()
    }
}