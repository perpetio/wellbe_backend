package com.perpetio.exception

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.statusPages() {
    install(StatusPages) {
        exception<RegistrationNameException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, "Username already has taken")
        }
        exception<RegistrationFieldLengthException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, "Username length must be > 3 and password > 5")
        }
        exception<LoginException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, "Invalid username or password")
        }
        exception<PostEditException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, "Invalid post id")
        }
        exception<MemberAlreadyJoinException> { call, _ ->
            call.respond(HttpStatusCode.Conflict, "User with this id has already joined room")
        }
        exception<ChatNotFindException> { call, _ ->
            call.respond(HttpStatusCode.NotFound, "Can not find room with this id")
        }
        exception<ChatAlreadyJoinedException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, "You have already joined this chat")
        }
        exception<MultiPartDataNotFoundException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, "File not found or wrong extension")
        }
        exception<PostAlreadyLikedException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, "Post has already liked or unliked")
        }
    }
}

class RegistrationNameException : RuntimeException()
class RegistrationFieldLengthException : RuntimeException()
class LoginException : RuntimeException()
class PostEditException : RuntimeException()
class MemberAlreadyJoinException : RuntimeException()
class ChatNotFindException : RuntimeException()
class ChatAlreadyJoinedException : RuntimeException()
class MultiPartDataNotFoundException : RuntimeException()
class PostAlreadyLikedException : RuntimeException()