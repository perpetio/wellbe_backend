package com.perpetio.di

import com.perpetio.controller.PostController
import com.perpetio.repository.post.PostRepository
import com.perpetio.repository.post.PostRepositoryImpl
import com.perpetio.repository.postLike.PostLikeRepository
import com.perpetio.repository.postLike.PostLikeRepositoryImpl
import org.koin.dsl.module

val postModule = module {
    single<PostRepository> {
        PostRepositoryImpl(get())
    }
    single<PostLikeRepository> {
        PostLikeRepositoryImpl(get())
    }
    single {
        PostController(get(), get())
    }
}