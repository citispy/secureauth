package com.example.routes

import com.example.common.Constants
import com.example.domain.model.Endpoint
import com.example.domain.model.response.ApiResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authorizeRoute() {
    authenticate(configurations = arrayOf(Constants.AUTH_SESSION)) {
        get(Endpoint.Authorized.path) {
            call.respond(
                message = ApiResponse(
                    success = true,
                    message = "User successfully verified"
                ),
                status = HttpStatusCode.OK
            )
        }
    }
}