package com.perpetio.repository.post

import com.perpetio.dto.post.CreateEditPostModel
import com.perpetio.dto.post.PostModel
import com.perpetio.util.SearchFilter
import com.perpetio.util.pagination.Pagination
import com.perpetio.util.pagination.PaginationResult

interface PostRepository {

    fun editPost(userId: Long, post: CreateEditPostModel)

    fun createPost(userId: Long, post: CreateEditPostModel): Long

    fun removePost(userId: Long, postId: Long)

    fun selectUserPosts(userId: Long, pagination: Pagination, search: SearchFilter?): PaginationResult<PostModel>

    fun selectUserFavouritePosts(
        userId: Long,
        pagination: Pagination,
        search: SearchFilter?
    ): PaginationResult<PostModel>

    fun selectFeedPosts(userId: Long, pagination: Pagination, search: SearchFilter?): PaginationResult<PostModel>

    fun selectPopularPosts(userId: Long, pagination: Pagination, search: SearchFilter?): PaginationResult<PostModel>

    fun incrementLikeCounter(postId: Long)

    fun decrementLikeCounter(postId: Long)
}