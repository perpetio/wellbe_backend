package com.perpetio.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UserMessageModel(
    val id: Long,
    val username: String,
    var avatarModel: AvatarModel?
)