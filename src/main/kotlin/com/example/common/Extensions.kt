package com.example.common

import com.example.domain.model.Endpoint
import com.example.domain.model.response.ApiResponse
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun GoogleIdToken.getValue(key: String) = payload[key].toString()

suspend fun RoutingContext.redirectToUnauthorizedRoute(app: Application, logMsg: String) {
    app.log.info(logMsg)
    call.respondRedirect(Endpoint.Unauthorized.path)
}

suspend fun RoutingContext.successResponse(
    app: Application,
    message: String?
) {
    app.log.info(message)
    call.respond(
        message = ApiResponse(
            success = true,
            message = message
        ),
        status = HttpStatusCode.OK
    )
}

suspend fun RoutingContext.failedResponse(
    app: Application,
    message: String?,
    statusCode: HttpStatusCode
) {
    app.log.info(message)
    call.respond(
        message = ApiResponse(
            success = false,
            message = message
        ),
        status = statusCode
    )
}