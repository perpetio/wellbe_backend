package com.perpetio.repository.post

import com.perpetio.database.Users
import com.perpetio.database.entity.PostUserLikes
import com.perpetio.database.entity.Posts
import com.perpetio.database.entity.RoomsToUsers
import com.perpetio.dto.post.CreateEditPostModel
import com.perpetio.dto.post.PostModel
import com.perpetio.dto.post.UserPostModel
import com.perpetio.dto.user.PostMemberModel
import com.perpetio.util.SearchFilter
import com.perpetio.util.pagination.Pagination
import com.perpetio.util.pagination.PaginationResult
import com.perpetio.util.pagination.createPaginationResult
import com.perpetio.util.pagination.firstIndex
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.count
import org.ktorm.entity.filter
import org.ktorm.entity.sequenceOf
import org.ktorm.expression.BinaryExpression
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PostRepositoryImpl(private val db: Database) : PostRepository {

    override fun editPost(userId: Long, post: CreateEditPostModel) {
        db.update(Posts) {
            set(it.title, post.title)
            set(it.text, post.text)
            set(it.visible, post.visible)
            set(it.tag, post.tag)
            set(it.backColor, post.backColor)
            set(it.sign, post.sign)
            set(it.signColor, post.signColor)
            where {
                it.id eq post.id!! and (it.userId eq userId)
            }
        }
    }

    override fun createPost(userId: Long, post: CreateEditPostModel): Long {
        return db.insertAndGenerateKey(Posts) {
            set(it.userId, userId)
            set(it.title, post.title)
            set(it.text, post.text)
            set(it.visible, post.visible)
            set(it.tag, post.tag)
            set(it.createdTime, LocalDateTime.now())
            set(it.backColor, post.backColor)
            set(it.sign, post.sign)
            set(it.signColor, post.signColor)
        } as Long
    }

    override fun removePost(userId: Long, postId: Long) {
        db.delete(Posts) { it.id eq postId and (it.userId eq userId) }.apply {
            db.delete(RoomsToUsers) { it.roomId eq postId }
        }
    }

    override fun selectUserPosts(
        userId: Long,
        pagination: Pagination,
        search: SearchFilter?
    ): PaginationResult<PostModel> {

        var condition = Posts.userId eq userId
        if (search != null) {
            condition = condition and (Posts.title like (search.search) or (Posts.text like (search.search)))
        }

        val list = db.from(Posts)
            .leftJoin(PostUserLikes, on = Posts.id eq PostUserLikes.postId and (PostUserLikes.userId eq userId))
            .leftJoin(Users, on = Posts.userId eq Users.id)
            .select()
            .where { condition }
            .orderBy(Posts.id.desc())
            .limit(pagination.size)
            .offset(pagination.firstIndex)
            .map {
                val visible = it[Posts.visible]!!
                val text = it[Posts.text]!!
                val title = it[Posts.title]!!
                val tag = it[Posts.tag]!!
                val id = it[Posts.id]!!
                val backColor = it[Posts.backColor]!!
                val likes = it[Posts.likes]!!
                val createdTime = it[Posts.createdTime]!!
                val sign = it[Posts.sign]!!
                val signColor = it[Posts.signColor]
                val liked = it[PostUserLikes.id] != null
                PostModel(
                    id,
                    UserPostModel(it[Users.id], it[Users.username], it[Users.avatarUrl]),
                    title,
                    text,
                    visible,
                    tag,
                    backColor,
                    likes,
                    PostMemberModel(member = true, liked),
                    createdTime.format(DateTimeFormatter.ISO_DATE_TIME),
                    sign,
                    signColor
                )
            }

        return list.createPaginationResult(pagination, getTotalRecords(condition))
    }

    override fun selectUserFavouritePosts(
        userId: Long,
        pagination: Pagination,
        search: SearchFilter?
    ): PaginationResult<PostModel> {
        var condition = PostUserLikes.userId eq userId
        if (search != null) {
            condition = condition and (Posts.title like (search.search) or (Posts.text like (search.search)))
        }

        val list = db.from(PostUserLikes)
            .leftJoin(Posts, on = PostUserLikes.postId eq Posts.id)
            .leftJoin(Users, on = PostUserLikes.userId eq Users.id)
            .leftJoin(RoomsToUsers, on = Posts.id eq RoomsToUsers.roomId and (RoomsToUsers.userId eq userId))
            .select()
            .where { condition }
            .orderBy(Posts.id.desc())
            .limit(pagination.size)
            .offset(pagination.firstIndex)
            .map {
                val visible = it[Posts.visible]!!
                val text = it[Posts.text]!!
                val title = it[Posts.title]!!
                val tag = it[Posts.tag]!!
                val id = it[Posts.id]!!
                val backColor = it[Posts.backColor]!!
                val likes = it[Posts.likes]!!
                val createdTime = it[Posts.createdTime]!!
                val sign = it[Posts.sign]!!
                val signColor = it[Posts.signColor]
                val liked = it[PostUserLikes.id] != null
                val member = it[RoomsToUsers.id] != null
                PostModel(
                    id,
                    UserPostModel(it[Users.id], it[Users.username], it[Users.avatarUrl]),
                    title,
                    text,
                    visible,
                    tag,
                    backColor,
                    likes,
                    PostMemberModel(member, liked),
                    createdTime.format(DateTimeFormatter.ISO_DATE_TIME),
                    sign,
                    signColor
                )
            }

        return list.createPaginationResult(pagination, getTotalRecordsFavourite(condition))
    }

    override fun selectFeedPosts(
        userId: Long,
        pagination: Pagination,
        search: SearchFilter?
    ): PaginationResult<PostModel> {

        var condition = Posts.visible eq true
        if (search != null) {
            condition = condition and (Posts.title like (search.search) or (Posts.text like (search.search)))
        }

        val list = db.from(Posts)
            .leftJoin(RoomsToUsers, on = Posts.id eq RoomsToUsers.roomId and (RoomsToUsers.userId eq userId))
            .leftJoin(PostUserLikes, on = Posts.id eq PostUserLikes.postId and (PostUserLikes.userId eq userId))
            .leftJoin(Users, on = Posts.userId eq Users.id)
            .select()
            .where { condition }
            .orderBy(Posts.id.desc())
            .limit(pagination.size)
            .offset(pagination.firstIndex)
            .map {
                val visible = it[Posts.visible]!!
                val text = it[Posts.text]!!
                val title = it[Posts.title]!!
                val tag = it[Posts.tag]!!
                val id = it[Posts.id]!!
                val backColor = it[Posts.backColor]!!
                val likes = it[Posts.likes]!!
                val createdTime = it[Posts.createdTime]!!
                val sign = it[Posts.sign]!!
                val signColor = it[Posts.signColor]
                val member = it[RoomsToUsers.id] != null
                val liked = it[PostUserLikes.id] != null
                PostModel(
                    id,
                    UserPostModel(it[Users.id], it[Users.username], it[Users.avatarUrl]),
                    title,
                    text,
                    visible,
                    tag,
                    backColor,
                    likes,
                    PostMemberModel(member, liked),
                    createdTime.format(DateTimeFormatter.ISO_DATE_TIME),
                    sign,
                    signColor
                )
            }

        return list.createPaginationResult(pagination, getTotalRecords(condition))
    }

    override fun selectPopularPosts(
        userId: Long,
        pagination: Pagination,
        search: SearchFilter?
    ): PaginationResult<PostModel> {

        var condition = Posts.visible eq true
        if (search != null) {
            condition = condition and (Posts.title like (search.search) or (Posts.text like (search.search)))
        }

        val list = db.from(Posts)
            .leftJoin(RoomsToUsers, on = Posts.id eq RoomsToUsers.roomId and (RoomsToUsers.userId eq userId))
            .leftJoin(PostUserLikes, on = Posts.id eq PostUserLikes.postId and (PostUserLikes.userId eq userId))
            .leftJoin(Users, on = Posts.userId eq Users.id)
            .select()
            .where { condition }
            .orderBy(Posts.likes.desc(), Posts.id.desc())
            .limit(pagination.size)
            .offset(pagination.firstIndex)
            .map {
                val visible = it[Posts.visible]!!
                val text = it[Posts.text]!!
                val title = it[Posts.title]!!
                val tag = it[Posts.tag]!!
                val id = it[Posts.id]!!
                val backColor = it[Posts.backColor]!!
                val likes = it[Posts.likes]!!
                val createdTime = it[Posts.createdTime]!!
                val sign = it[Posts.sign]!!
                val signColor = it[Posts.signColor]
                val member = it[RoomsToUsers.id] != null
                val liked = it[PostUserLikes.id] != null
                PostModel(
                    id,
                    UserPostModel(it[Users.id], it[Users.username], it[Users.avatarUrl]),
                    title,
                    text,
                    visible,
                    tag,
                    backColor,
                    likes,
                    PostMemberModel(member, liked),
                    createdTime.format(DateTimeFormatter.ISO_DATE_TIME),
                    sign,
                    signColor
                )
            }

        return list.createPaginationResult(pagination, getTotalRecords(condition))
    }

    override fun incrementLikeCounter(postId: Long) {
        db.update(Posts) {
            set(it.likes, it.likes + 1)
            where { it.id eq postId }
        }
    }

    override fun decrementLikeCounter(postId: Long) {
        db.update(Posts) {
            set(it.likes, it.likes - 1)
            where { it.id eq postId }
        }
    }

    private fun getTotalRecords(condition: BinaryExpression<Boolean>): Long {
        return db.sequenceOf(Posts).filter { condition }.count().toLong()
    }

    private fun getTotalRecordsFavourite(condition: BinaryExpression<Boolean>): Long {
        return db.sequenceOf(PostUserLikes).filter { condition }.count().toLong()
    }

}