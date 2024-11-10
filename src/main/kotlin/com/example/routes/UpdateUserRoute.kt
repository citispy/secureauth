package com.example.routes

import com.example.common.Constants
import com.example.common.failedResponse
import com.example.common.redirectToUnauthorizedRoute
import com.example.common.successResponse
import com.example.domain.model.Endpoint
import com.example.domain.model.UserSession
import com.example.domain.model.requets.UserUpdateRequest
import com.example.domain.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.updateUserRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    authenticate(Constants.AUTH_SESSION) {
        put(Endpoint.UpdateUserInfo.path) {
            val userSession = call.principal<UserSession>()
            if (userSession == null) {
                redirectToUnauthorizedRoute(
                    app = app,
                    logMsg = "Invalid Session"
                )
                return@put
            }

            val userUpdate = call.receive<UserUpdateRequest>()
            if (userUpdate.firstName.isBlank() || userUpdate.lastName.isBlank()) {
                failedResponse(
                    app = app,
                    message = "Error updating user",
                    statusCode = HttpStatusCode.BadRequest
                )
                return@put
            }
            try {
                val updated = userDataSource.updateUserInfo(
                    userId = userSession.id,
                    firstName = userUpdate.firstName,
                    lastName = userUpdate.lastName
                )

                if (!updated) {
                    failedResponse(
                        app = app,
                        message = "Error updating user",
                        statusCode = HttpStatusCode.BadRequest
                    )
                    return@put
                }

                successResponse(
                    app = app,
                    message = "User successfully updated"
                )

            } catch (e: Exception) {
                redirectToUnauthorizedRoute(
                    app = app,
                    logMsg = "Error updating user ${e.message}"
                )
            }
        }
    }
}