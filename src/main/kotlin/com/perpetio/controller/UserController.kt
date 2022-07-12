package com.perpetio.controller

import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.perpetio.dto.user.*
import com.perpetio.exception.LoginException
import com.perpetio.exception.MultiPartDataNotFoundException
import com.perpetio.exception.RegistrationFieldLengthException
import com.perpetio.exception.RegistrationNameException
import com.perpetio.repository.user.UserRepository
import com.perpetio.util.AmazonS3Client
import com.perpetio.util.EncoderUtils.decryptCBC
import com.perpetio.util.EncoderUtils.encryptCBC
import com.perpetio.util.TokenManager
import io.ktor.http.content.*
import java.io.InputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class UserController(
    private val userRepo: UserRepository,
    private val tokenManager: TokenManager,
    private val amazonClient: AmazonS3Client
) {

    fun register(model: UserRegistrationModel): UserCredModel {

        val userByUserName = userRepo.getUserByEmail(model.email)

        if (!model.isValidCredentials()) {
            throw RegistrationFieldLengthException()
        }

        if (userByUserName != null) {
            throw RegistrationNameException()
        }

        val userId = userRepo.createUser(model.username, model.password.encryptCBC(), model.email)

        val token = tokenManager.generateJWTToken(UserCredModel(userId, model.email))

        return UserCredModel(userId, model.username, token = token, mail = model.email)
    }

    fun login(model: UserLoginModel): UserCredModel {

        val userByEmail = userRepo.getUserByEmail(model.email)

        if (userByEmail == null || userByEmail.password.decryptCBC() != model.password) {
            throw LoginException()
        }

        val token = tokenManager.generateJWTToken(UserCredModel(userByEmail.id, userByEmail.mail))

        return UserCredModel.fromUserEntity(userByEmail).apply {
            this.token = token
            avatarModel = getAvatar(userByEmail.id)
        }
    }

    fun updateUser(model: UserUpdatingModel, userId: Long) {
        userRepo.updateUser(model, userId)
    }

    suspend fun uploadAvatar(photo: MultiPartData, userId: Long): AvatarModel {

        var resInputStream: InputStream? = null
        var ext: String? = null
        photo.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    resInputStream = part.streamProvider()
                    ext = part.originalFileName
                }
                else -> {
                    throw MultiPartDataNotFoundException()
                }
            }
        }

        val extension = try {
            "." + ext?.split(".")?.get(1)
        } catch (ext: Exception) {
            ""
        }

        val key = UUID.randomUUID().toString() + System.currentTimeMillis() + extension
        val por = PutObjectRequest(
            "perpetio-ktor-chat",
            key,
            resInputStream,
            ObjectMetadata()
        )
        amazonClient.s3Client.putObject(por)

        val avatarPresignedUrl = getAvatarPresignedUrl(key)
        userRepo.updateUserAvatar(userId, avatarPresignedUrl, key)
        return AvatarModel(avatarPresignedUrl)
    }

    fun getAvatar(userId: Long): AvatarModel {
        val userById = userRepo.getUserById(userId)
        userById?.avatarUrl?.let {
            var avatarUrl = userById.avatarUrl
            if (userById.avatarUpdateTime != null && userById.avatarUpdateTime!!.plusDays(6) < LocalDateTime.now()) {
                val avatarPresignedUrl = getAvatarPresignedUrl(userById.avatarId!!)
                userRepo.updateUserAvatar(userId, avatarPresignedUrl, userById.avatarId!!)
                avatarUrl = avatarPresignedUrl
            }
            return AvatarModel(avatarUrl)
        } ?: return AvatarModel(null)
    }

    private fun getAvatarPresignedUrl(keyName: String): String {

        val expiration = LocalDateTime.now().plusDays(6)

        val generatePresignedUrlRequest = GeneratePresignedUrlRequest("perpetio-ktor-chat", keyName)
            .withMethod(com.amazonaws.HttpMethod.GET)
            .withExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))

        return amazonClient.s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString()
    }
}