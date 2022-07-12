package com.perpetio.controller

import com.perpetio.dto.chat.MemberModel
import com.perpetio.dto.chat.MessageModel
import com.perpetio.dto.post.PostModel
import com.perpetio.dto.user.AvatarModel
import com.perpetio.dto.user.UserCredModel
import com.perpetio.dto.user.UserMessageModel
import com.perpetio.exception.ChatAlreadyJoinedException
import com.perpetio.exception.MemberAlreadyJoinException
import com.perpetio.repository.message.MessageRepository
import com.perpetio.repository.roomuser.RoomUserRepository
import com.perpetio.repository.user.UserRepository
import com.perpetio.util.pagination.Pagination
import com.perpetio.util.pagination.PaginationResult
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ChatController(
    private val messageRepo: MessageRepository,
    private val userRepo: UserRepository,
    private val roomUserRepository: RoomUserRepository
) {

    private val roomMemberConnection = Collections.synchronizedMap(HashMap<Long, HashSet<MemberModel?>>())

    fun onJoin(
        roomId: Long,
        userId: Long,
        socket: DefaultWebSocketSession
    ) {
        val currentRoomConnection = roomMemberConnection[roomId] ?: HashSet()
        if (currentRoomConnection.firstOrNull { memberModel -> memberModel?.userId == userId } != null) {
            throw MemberAlreadyJoinException()
        }
        val memberModel = MemberModel(userId, socket)
        currentRoomConnection.add(memberModel)
        roomMemberConnection[roomId] = currentRoomConnection
    }

    suspend fun sendMessage(sender: UserCredModel?, message: String, roomId: Long, userId: Long) {
        val messageEntity = MessageModel(
            text = message,
            roomId = roomId,
            userId = userId,
            createdTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
            user = UserMessageModel(userId, sender?.username ?: "Guest", AvatarModel(sender?.avatarModel?.avatarUrl))
        )
        messageRepo.createMessage(messageEntity)
        val allMembersInRoom = roomMemberConnection[roomId]
        val parsedMessage = Json.encodeToString(MessageModel.serializer(), messageEntity)
        allMembersInRoom?.forEach {
            it?.socket?.send(Frame.Text(parsedMessage))
        }
    }

    suspend fun tryDisconnect(userId: Long, roomId: Long) {
        val allMembersInRoom = roomMemberConnection[roomId]
        val definedMember = allMembersInRoom?.first { memberModel -> memberModel?.userId == userId }
        definedMember?.socket?.close()
        allMembersInRoom?.remove(definedMember)
    }

    fun getAllMessagesByRoom(roomId: Long, pagination: Pagination): PaginationResult<MessageModel> {
        return messageRepo.getAllMessageByRoomId(roomId, pagination)
    }

    fun getUser(userId: Long): UserCredModel? {
        userRepo.getUserById(userId)?.let {
            return UserCredModel.fromUserEntity(it)
        }
        return null
    }

    fun getAllRoomsByUser(roomId: Long, pagination: Pagination): PaginationResult<PostModel> {
        return roomUserRepository.getUserRoomList(roomId, pagination)
    }

    fun addUserToRoom(userId: Long, roomId: Long) {
        roomUserRepository.isUserAlreadyJoinedRoom(userId, roomId).let {
            if (it) {
                throw ChatAlreadyJoinedException()
            } else {
                roomUserRepository.addRoomForUser(userId, roomId)
            }
        }
    }

    fun removeUserFromRoom(userId: Long, roomId: Long) {
        roomUserRepository.removeRoomFromUser(userId, roomId)
    }
}