package com.example.common

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken

fun GoogleIdToken.getValue(key: String) = payload[key].toString()