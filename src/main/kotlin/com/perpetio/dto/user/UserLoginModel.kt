package com.perpetio.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginModel(
    val email: String,
    val password: String
)
