package com.example.routes

import com.example.common.Constants
import com.example.domain.model.response.GetUserResponse
import com.example.domain.model.Endpoint
import com.example.domain.model.UserSession
import com.example.domain.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getUserInfoRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    // This route can only be accessed if authenticated
    authenticate(Constants.AUTH_SESSION) {
        get(Endpoint.GetUserInfo.path) {
            val userSession = call.principal<UserSession>()
            if (userSession == null) {
                app.log.info("Invalid Session")
                call.respondRedirect(Endpoint.Unauthorized.path)
                return@get
            }

            try {
                call.respond(
                    message = GetUserResponse(
                        success = true,
                        user = userDataSource.getUserInfo(userSession.id)
                    ),
                    status = HttpStatusCode.OK
                )
            } catch (e: Exception) {
                app.log.info("Getting user info error: ${e.message}")
                call.respondRedirect(Endpoint.Unauthorized.path)
            }
        }
    }
}