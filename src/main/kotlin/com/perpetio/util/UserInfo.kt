package com.perpetio.util

import io.ktor.server.auth.jwt.*


object UserInfo {

    fun getId(principal: JWTPrincipal?): Long {
        return principal?.payload!!.getClaim("id").asLong()
    }
}