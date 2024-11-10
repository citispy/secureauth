package com.example.domain.model.requets

import kotlinx.serialization.Serializable

@Serializable
data class TokenVerificationRequest(
    val tokenId: String
)
