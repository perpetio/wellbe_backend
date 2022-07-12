package com.perpetio.database

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import org.ktorm.database.Database

private val appConfig = HoconApplicationConfig(ConfigFactory.load())

private val dbUrl = appConfig.property("db.jdbcUrl").getString()
private val dbDriver = appConfig.property("db.jdbcDriver").getString()
private val dbUser = appConfig.property("db.dbUser").getString()
private val dbPassword = appConfig.property("db.dbPassword").getString()

object DatabaseConnection {
    val database = Database.connect(
        dbUrl,
        dbDriver,
        dbUser,
        dbPassword
    )
}