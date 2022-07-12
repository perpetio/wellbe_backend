package com.perpetio


import com.perpetio.di.mainModule
import com.perpetio.di.postModule
import com.perpetio.di.roomModule
import com.perpetio.di.userModule
import com.perpetio.exception.statusPages
import com.perpetio.plugins.configureSecurity
import com.perpetio.plugins.configureSerialization
import com.perpetio.plugins.configureSockets
import com.perpetio.routing.configureRouting
import com.perpetio.util.TokenManager
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit =
    EngineMain.main(args)

fun Application.module() {
    install(Koin) {
        modules(mainModule, userModule, postModule, roomModule)
    }
    val tokenManager: TokenManager by inject()
    configureSecurity(tokenManager)
    configureSockets()
    configureRouting()
    configureSerialization()
    statusPages()
}

