package com.example.routes

import com.example.common.Constants
import com.example.common.failedResponse
import com.example.common.redirectToUnauthorizedRoute
import com.example.common.successResponse
import com.example.domain.model.Endpoint
import com.example.domain.model.UserSession
import com.example.domain.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.deleteUserRoute(app: Application, userDataSource: UserDataSource) {
    authenticate(Constants.AUTH_SESSION) {
        delete(Endpoint.DeleteUser.path) {
            val userSession = call.principal<UserSession>()
            if (userSession == null) {
                redirectToUnauthorizedRoute(
                    app = app,
                    logMsg = "Invalid Session"
                )
                return@delete
            }

            try {
                val deleted = userDataSource.deleteUser(userId = userSession.id)
                if (!deleted) {
                    failedResponse(
                        app = app,
                        message = "Error deleting user",
                        statusCode = HttpStatusCode.BadRequest
                    )
                    return@delete
                }
                call.sessions.clear<UserSession>()
                successResponse(
                    app = app,
                    message = "User successfully deleted"
                )
            } catch (e: Exception) {
                redirectToUnauthorizedRoute(app, "Error deleting user")
            }
        }
    }
}