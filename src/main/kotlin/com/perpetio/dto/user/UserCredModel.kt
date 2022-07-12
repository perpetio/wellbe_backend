package com.perpetio.dto.user

import com.perpetio.database.User
import kotlinx.serialization.Serializable

@Serializable
data class UserCredModel(
    val id: Long,
    val username: String?,
    val mail: String,
    val country: String?,
    val city: String?,
    var token: String?,
    var avatarModel: AvatarModel
) {
    constructor(id: Long, mail: String) : this(id, null, mail, null, null, null, AvatarModel(null))
    constructor(id: Long, username: String, token: String?, mail: String) : this(
        id,
        username,
        mail,
        null,
        null,
        token,
        AvatarModel(null)
    )

    companion object {
        fun fromUserEntity(user: User): UserCredModel {
            return UserCredModel(
                user.id,
                user.username,
                user.mail,
                user.country,
                user.city,
                null,
                AvatarModel(user.avatarUrl)
            )
        }
    }
}