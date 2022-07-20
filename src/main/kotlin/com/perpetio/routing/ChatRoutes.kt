package com.perpetio.routing.user

import com.perpetio.controller.ChatController
import com.perpetio.plugins.exception.ChatNotFindException
import com.perpetio.util.UserInfo
import com.perpetio.util.pagination.handlePagination
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*

fun Route.chatSocket(roomController: ChatController) {

    authenticate {
        webSocket("/chat-socket/{room}") {
            val roomId = call.parameters["room"]?.toLong() ?: 0
            val userId = UserInfo.getId(call.principal())
            val userModel = roomController.getUser(userId)
            roomController.onJoin(roomId = roomId, userId, this)
            try {
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    roomController.sendMessage(userModel, receivedText, roomId, userId)
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                roomController.tryDisconnect(userId, roomId)
            }
        }

        route("chat/messages/{room}") {
            get {
                val roomId = call.parameters["room"]?.toLong() ?: 0
                val queryParams = call.request.queryParameters
                call.respond(
                    HttpStatusCode.OK,
                    roomController.getAllMessagesByRoom(roomId, handlePagination(queryParams))
                )
            }
        }

        route("chat/{room}") {
            post {
                call.parameters["room"]?.toLong()?.let {
                    roomController.addUserToRoom(UserInfo.getId(call.principal()), it)
                    call.respond(HttpStatusCode.OK, true)
                } ?: throw ChatNotFindException()
            }

            delete {
                call.parameters["room"]?.toLong()?.let {
                    roomController.removeUserFromRoom(UserInfo.getId(call.principal()), it)
                    call.respond(HttpStatusCode.OK, true)
                } ?: throw ChatNotFindException()
            }
        }

        route("chats") {
            get {
                val queryParams = call.request.queryParameters
                call.respond(
                    HttpStatusCode.OK,
                    roomController.getAllRoomsByUser(
                        UserInfo.getId(call.principal()),
                        handlePagination(queryParams)
                    )
                )
            }
        }
    }
}
