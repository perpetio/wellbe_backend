package com.perpetio.repository.roomuser

import com.perpetio.dto.post.PostModel
import com.perpetio.util.pagination.Pagination
import com.perpetio.util.pagination.PaginationResult

interface RoomUserRepository {

    fun addRoomForUser(userId: Long, roomId: Long)

    fun removeRoomFromUser(userId: Long, roomId: Long)

    fun getUserRoomList(userId: Long, pagination: Pagination): PaginationResult<PostModel>

    fun isUserAlreadyJoinedRoom(userId: Long, roomId: Long): Boolean
}