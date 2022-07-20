package com.perpetio


import com.perpetio.di.mainModule
import com.perpetio.di.postModule
import com.perpetio.di.roomModule
import com.perpetio.di.userModule
import com.perpetio.plugins.exception.configureStatusPagesPlugin
import com.perpetio.plugins.configureSecurity
import com.perpetio.plugins.configureSerializationPlugin
import com.perpetio.plugins.configureSocketsPlugin
import com.perpetio.routing.configureRoutingPlugin
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit =
    EngineMain.main(args)

fun Application.module() {

    install(Koin) {
        modules(mainModule, userModule, postModule, roomModule)
    }

    configureSecurity()
    configureSocketsPlugin()
    configureRoutingPlugin()
    configureSerializationPlugin()
    configureStatusPagesPlugin()
}

