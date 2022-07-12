package com.perpetio.repository.message

import com.perpetio.dto.chat.MessageModel
import com.perpetio.util.pagination.Pagination
import com.perpetio.util.pagination.PaginationResult

interface MessageRepository {

    fun createMessage(message: MessageModel)

    fun getAllMessageByRoomId(roomId: Long, pagination: Pagination): PaginationResult<MessageModel>
}