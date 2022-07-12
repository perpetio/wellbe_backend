package com.perpetio.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class PostMemberModel(val member: Boolean?, val liked: Boolean = false)
