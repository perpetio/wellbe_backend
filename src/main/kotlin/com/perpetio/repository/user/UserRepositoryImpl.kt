package com.perpetio.repository.user

import com.perpetio.database.User
import com.perpetio.database.Users
import com.perpetio.dto.user.UserUpdatingModel
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.update
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import java.time.LocalDateTime

class UserRepositoryImpl(private val db: Database) : UserRepository {

    override fun createUser(username: String, password: String, email: String): Long {
        return db.insertAndGenerateKey(Users) {
            set(it.username, username)
            set(it.password, password)
            set(it.mail, email)
            set(it.createdTime, LocalDateTime.now())
        } as Long
    }

    override fun getUserByEmail(email: String): User? {
        return db.sequenceOf(Users).firstOrNull { it.mail eq email }
    }

    override fun updateUser(model: UserUpdatingModel, userId: Long) {
        db.update(Users) {
            set(it.username, model.username)
            set(it.country, model.country)
            set(it.city, model.city)
            where { it.id eq userId }
        }
    }

    override fun getUserById(userId: Long): User? {
        return db.sequenceOf(Users).firstOrNull { it.id eq userId }
    }

    override fun updateUserAvatar(userId: Long, avatarUrl: String, avatarId: String) {
        db.update(Users) {
            set(it.avatarUrl, avatarUrl)
            set(it.avatarUpdateTime, LocalDateTime.now())
            set(it.avatarId, avatarId)
            where { it.id eq userId }
        }
    }
}

