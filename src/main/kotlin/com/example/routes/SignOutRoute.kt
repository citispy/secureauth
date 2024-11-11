package com.example.routes

import com.example.common.Constants
import com.example.common.successResponse
import com.example.domain.model.Endpoint
import com.example.domain.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.signOutRoute(app: Application) {
    authenticate(Constants.AUTH_SESSION) {
        get(Endpoint.SignOut.path) {
            call.sessions.clear<UserSession>()
            successResponse(app, "Successfully signed out")
        }
    }
}