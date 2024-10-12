package com.example.domain.model

sealed class Endpoint(val path: String) {
    data object Root: Endpoint(path = "/")
    data object TokenVerification: Endpoint(path = "/token/verification")
    data object SignOut: Endpoint(path = "/sign-out")
    data object Unauthorized: Endpoint(path = "/unauthorized")
    data object Authorized: Endpoint(path = "/authorized")
    // Users
    data object GetUserInfo: Endpoint(path = "/users/get")
    data object UpdateUserInfo: Endpoint(path = "/users/update")
    data object DeleteUser: Endpoint(path = "/users/delete")
}