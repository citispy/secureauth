package com.example.plugins

import com.example.domain.repository.UserDataSource
import com.example.routes.authorizeRoute
import com.example.routes.rootRoute
import com.example.routes.tokenVerificationRoute
import com.example.routes.unauthorizedRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

fun Application.configureRouting() {
    routing {
        val userDataSource: UserDataSource by inject(UserDataSource::class.java)
        rootRoute()
        authorizeRoute()
        tokenVerificationRoute(application, userDataSource)
        unauthorizedRoute()
    }
}
