package com.perpetio.repository.user

import com.perpetio.database.User
import com.perpetio.dto.user.UserUpdatingModel

interface UserRepository {

    fun createUser(username: String, password: String, email: String): Long

    fun getUserByEmail(email: String): User?

    fun updateUser(model: UserUpdatingModel, userId: Long)

    fun getUserById(userId: Long): User?

    fun updateUserAvatar(userId: Long, avatarUrl: String, avatarId: String)
}