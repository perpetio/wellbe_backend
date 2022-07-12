package com.perpetio.di

import com.perpetio.controller.UserController
import com.perpetio.repository.user.UserRepository
import com.perpetio.repository.user.UserRepositoryImpl
import com.perpetio.util.AmazonS3Client
import org.koin.dsl.module

val userModule = module {
    single {
        AmazonS3Client(get())
    }
    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    single {
        UserController(get(), get(), get())
    }
}