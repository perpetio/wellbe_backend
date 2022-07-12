package com.perpetio.dto.chat

import io.ktor.websocket.*
import kotlinx.serialization.Serializable

@Serializable
data class MemberModel(
    val userId: Long,
    val socket: DefaultWebSocketSession
)
