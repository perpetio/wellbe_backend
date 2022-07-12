package com.perpetio.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class CreateEditPostModel(
    var id: Long? = null,
    val title: String,
    val text: String,
    val visible: Boolean,
    val tag: String?,
    val backColor: String,
    val likes: Long,
    val sign: Int,
    val signColor: String?
)