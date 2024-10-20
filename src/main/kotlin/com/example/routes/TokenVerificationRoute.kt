package com.example.routes

import com.example.common.Constants
import com.example.common.getValue
import com.example.domain.model.ApiRequest
import com.example.domain.model.Endpoint
import com.example.domain.model.UserSession
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.tokenVerificationRoute(app: Application) {
    post(Endpoint.TokenVerification.path) {
        val request = call.receive<ApiRequest>()
        if (request.tokenId.isNotBlank()) {
            val result = verifyGoogleTokenId(tokenId = request.tokenId)
            if (result == null) {
                redirectToUnauthorizedRoute(
                    app = app,
                    logMsg = "Token verification failed"
                )
            } else {
                redirectToAuthorizedRoute(result, app)
            }
        } else {
            redirectToUnauthorizedRoute(
                app = app,
                logMsg = "Empty token ID"
            )
        }
    }
}

private suspend fun RoutingContext.redirectToUnauthorizedRoute(app: Application, logMsg: String) {
    app.log.info(logMsg)
    call.respondRedirect(Endpoint.Unauthorized.path)
}

private suspend fun RoutingContext.redirectToAuthorizedRoute(
    result: GoogleIdToken,
    app: Application
) {
    val sub = result.getValue("sub")
    val name = result.getValue("name")
    val email = result.getValue("picture")
    val profilePhoto = result.getValue("picture")
    app.log.info("Token verification success: $name $email")
    call.sessions.set(UserSession(id = "123", name = "Yusuf"))
    call.respondRedirect(Endpoint.Authorized.path)
}

private fun verifyGoogleTokenId(tokenId: String): GoogleIdToken? {
    return try {
        GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(listOf(Constants.AUDIENCE))
            .setIssuer(Constants.ISSUER)
            .build().run {
                verify(tokenId)
            }
    } catch (e: Exception) {
        null
    }
}