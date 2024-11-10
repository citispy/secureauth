package com.example.domain.model.response

import com.example.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class GetUserResponse(
    val success: Boolean,
    val user: User? = null
)
