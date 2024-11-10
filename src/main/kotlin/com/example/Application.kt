package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    embeddedServer(Netty, host = "127.0.0.1", port = 8080) {
        module()
    }.start(wait = true)
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
