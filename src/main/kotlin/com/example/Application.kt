package com.example

import com.example.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Koin needs to be configured first
    configureKoin()
    // configureAuth() needs to be called before configureRouting()
    configureAuth()
    configureRouting()
    configureSerialization()
    configureMonitoring()
    configureSession()
}
