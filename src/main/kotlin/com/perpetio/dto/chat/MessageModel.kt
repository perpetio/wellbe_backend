package com.perpetio.dto.chat

import com.perpetio.dto.user.UserMessageModel
import kotlinx.serialization.Serializable

@Serializable
data class MessageModel(
    val id: Long? = null,
    val userId: Long,
    val roomId: Long,
    val text: String,
    val createdTime: String,
    var user: UserMessageModel
)
