package com.perpetio.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class UserPostModel(private val userId: Long?, private val username: String?, private val avatarUrl: String?)
