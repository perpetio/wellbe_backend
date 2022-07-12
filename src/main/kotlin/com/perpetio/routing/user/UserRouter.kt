package com.perpetio.routing

import com.perpetio.controller.UserController
import com.perpetio.dto.user.UserLoginModel
import com.perpetio.dto.user.UserRegistrationModel
import com.perpetio.dto.user.UserUpdatingModel
import com.perpetio.util.UserInfo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.userRoutes(userController: UserController) {

    post("/register") {
        val userCredentials = call.receive<UserRegistrationModel>()
        call.respond(HttpStatusCode.OK, userController.register(userCredentials))
    }

    post("/login") {
        val userCredentials = call.receive<UserLoginModel>()
        call.respond(HttpStatusCode.OK, userController.login(userCredentials))
    }

    authenticate {
        put("/user") {
            val userUpdatingModel = call.receive<UserUpdatingModel>()
            userController.updateUser(userUpdatingModel, UserInfo.getId(call.principal()))
            call.respond(HttpStatusCode.OK, true)
        }

        route("avatar") {
            post {
                val multipartData = call.receiveMultipart()
                call.respond(
                    HttpStatusCode.OK,
                    userController.uploadAvatar(multipartData, UserInfo.getId(call.principal()))
                )
            }
            get {
                call.respond(
                    HttpStatusCode.OK,
                    userController.getAvatar(UserInfo.getId(call.principal()))
                )
            }
        }
    }
}