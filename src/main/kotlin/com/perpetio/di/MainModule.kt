package com.perpetio.di

import com.perpetio.database.DatabaseConnection
import com.perpetio.util.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import org.koin.dsl.module

val mainModule = module {
    single {
        DatabaseConnection.database
    }
    single {
        HoconApplicationConfig(ConfigFactory.load())
    }
    single {
        TokenManager(get())
    }
}