package com.example.plugins

import com.example.routes.authorizeRoute
import com.example.routes.rootRoute
import com.example.routes.tokenVerificationRoute
import com.example.routes.unauthorizedRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        rootRoute()
        authorizeRoute()
        tokenVerificationRoute()
        unauthorizedRoute()
    }
}
