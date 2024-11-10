package com.example.domain.model.requets

import kotlinx.serialization.Serializable

@Serializable
data class UserUpdateRequest(
    val firstName: String,
    val lastName: String
)
