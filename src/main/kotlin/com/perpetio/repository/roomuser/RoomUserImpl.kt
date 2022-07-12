package com.perpetio.repository.roomuser

import com.perpetio.database.entity.RoomsToUsers
import com.perpetio.dto.post.PostModel
import com.perpetio.dto.post.UserPostModel
import com.perpetio.dto.user.PostMemberModel
import com.perpetio.util.pagination.Pagination
import com.perpetio.util.pagination.PaginationResult
import com.perpetio.util.pagination.createPaginationResult
import com.perpetio.util.pagination.firstIndex
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.count
import org.ktorm.entity.filter
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.expression.BinaryExpression
import java.time.format.DateTimeFormatter

class RoomUserImpl(private val db: Database) : RoomUserRepository {

    override fun addRoomForUser(userId: Long, roomId: Long) {
        db.insert(RoomsToUsers) {
            set(it.roomId, roomId)
            set(it.userId, userId)
        }
    }

    override fun removeRoomFromUser(userId: Long, roomId: Long) {
        db.delete(RoomsToUsers) { it.userId eq userId and (it.roomId eq roomId) }
    }

    override fun getUserRoomList(userId: Long, pagination: Pagination): PaginationResult<PostModel> {
        val condition = RoomsToUsers.userId eq userId
        val list = db.from(RoomsToUsers)
            .joinReferencesAndSelect()
            .where(condition)
            .orderBy(RoomsToUsers.id.desc())
            .limit(pagination.size)
            .offset(pagination.firstIndex)
            .map {
                val createEntity = RoomsToUsers.createEntity(it)
                val postModel = createEntity.room
                val user = postModel.user
                PostModel(
                    id = postModel.id,
                    user = UserPostModel(user.id, user.username, user.avatarUrl),
                    title = postModel.title,
                    text = postModel.text,
                    visible = postModel.visible,
                    backColor = postModel.backColor,
                    tag = postModel.tag,
                    likes = 0,
                    userStatus = PostMemberModel(true),
                    createdTime = postModel.createdTime.format(DateTimeFormatter.ISO_DATE_TIME),
                    sign = postModel.sign,
                    signColor = postModel.signColor
                )
            }
        return list.createPaginationResult(pagination, getTotalRecords(condition))
    }

    override fun isUserAlreadyJoinedRoom(userId: Long, roomId: Long): Boolean {
        db.sequenceOf(RoomsToUsers).firstOrNull { it.userId eq userId and (it.roomId eq roomId) }?.let {
            return true
        } ?: return false
    }

    private fun getTotalRecords(condition: BinaryExpression<Boolean>): Long {
        return db.sequenceOf(RoomsToUsers).filter { condition }.count().toLong()
    }
}