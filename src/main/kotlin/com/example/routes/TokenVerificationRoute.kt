package com.example.routes

import com.example.common.Constants
import com.example.common.getValue
import com.example.domain.model.ApiRequest
import com.example.domain.model.Endpoint
import com.example.domain.model.User
import com.example.domain.model.UserSession
import com.example.domain.repository.UserDataSource
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.tokenVerificationRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    post(Endpoint.TokenVerification.path) {
        val request = call.receive<ApiRequest>()
        if (request.tokenId.isBlank()) {
            redirectToUnauthorizedRoute(
                app = app,
                logMsg = "Empty token ID"
            )
            return@post
        }

        val result = verifyGoogleTokenId(tokenId = request.tokenId)
        if (result == null) {
            redirectToUnauthorizedRoute(
                app = app,
                logMsg = "Token verification failed"
            )
            return@post
        }

        val user = getUser(result, app)
        saveUserToDatabase(userDataSource, user).also { saved ->
            if (saved) {
                authorizeUser(app, user)
            } else {
                redirectToUnauthorizedRoute(app, "Error saving user")
            }
        }
    }
}

private suspend fun RoutingContext.redirectToUnauthorizedRoute(app: Application, logMsg: String) {
    app.log.info(logMsg)
    call.respondRedirect(Endpoint.Unauthorized.path)
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

/**
 * Returns a [User] from a [GoogleIdToken]
 */
private fun getUser(
    result: GoogleIdToken,
    app: Application
): User {
    val sub = result.getValue("sub")
    val name = result.getValue("name")
    val email = result.getValue("email")
    val profilePhoto = result.getValue("picture")
    app.log.info("Token verification success: $name $email")

    return User(
        id = sub,
        name = name,
        email = email,
        profilePhoto = profilePhoto
    )
}

private suspend fun saveUserToDatabase(
    userDataSource: UserDataSource,
    user: User,
): Boolean {
    return userDataSource.saveUserInfo(user)
}

private suspend fun RoutingContext.authorizeUser(app: Application, user: User) {
    app.log.info("User successfully saved/retrieved")
    call.sessions.set(UserSession(id = user.id, name = user.name))
    call.respondRedirect(Endpoint.Authorized.path)
}
