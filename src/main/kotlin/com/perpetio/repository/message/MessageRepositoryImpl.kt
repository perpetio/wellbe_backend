package com.perpetio.repository.message

import com.perpetio.database.entity.Messages
import com.perpetio.dto.chat.MessageModel
import com.perpetio.dto.user.AvatarModel
import com.perpetio.dto.user.UserMessageModel
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

class MessageRepositoryImpl(private val db: Database) : MessageRepository {

    override fun createMessage(message: MessageModel) {
        db.insertAndGenerateKey(Messages) {
            set(it.userId, message.userId)
            set(it.roomId, message.roomId)
            set(it.text, message.text)
            set(it.createdTime, LocalDateTime.now())
        }
    }

    override fun getAllMessageByRoomId(roomId: Long, pagination: Pagination): PaginationResult<MessageModel> {
        val condition = Messages.roomId eq roomId

        val list = db.from(Messages)
            .joinReferencesAndSelect()
            .where { condition }
            .orderBy(Messages.id.asc())
            .limit(pagination.size)
            .offset(pagination.firstIndex)
            .map {
                val createEntity = Messages.createEntity(it)
                MessageModel(
                    createEntity.id,
                    createEntity.user.id,
                    roomId,
                    createEntity.text,
                    createEntity.createdTime.format(DateTimeFormatter.ISO_DATE_TIME),
                    UserMessageModel(
                        createEntity.user.id, createEntity.user.username,
                        AvatarModel(createEntity.user.avatarUrl)
                    )
                )
            }
        return list.createPaginationResult(pagination, getTotalRecords(condition))
    }

    private fun getTotalRecords(condition: BinaryExpression<Boolean>): Long {
        return db.sequenceOf(Messages).filter { condition }.count().toLong()
    }
}