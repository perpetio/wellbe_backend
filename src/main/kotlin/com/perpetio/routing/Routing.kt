package com.perpetio.routing

import com.perpetio.controller.ChatController
import com.perpetio.controller.PostController
import com.perpetio.controller.UserController
import com.perpetio.routing.user.chatSocket
import com.perpetio.routing.user.postRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

    val userController: UserController by inject()
    val postController: PostController by inject()
    val roomController: ChatController by inject()

    install(Routing) {
        userRoutes(userController)
        postRoutes(postController)
        chatSocket(roomController)
    }
}