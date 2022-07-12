package com.perpetio.di

import com.perpetio.controller.ChatController
import com.perpetio.repository.message.MessageRepository
import com.perpetio.repository.message.MessageRepositoryImpl
import com.perpetio.repository.roomuser.RoomUserImpl
import com.perpetio.repository.roomuser.RoomUserRepository
import org.koin.dsl.module

val roomModule = module {
    single<MessageRepository> {
        MessageRepositoryImpl(get())
    }
    single<RoomUserRepository> {
        RoomUserImpl(get())
    }
    single {
        ChatController(get(), get(), get())
    }
}