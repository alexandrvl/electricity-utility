package com.electricity.utility.infrastructure.security.token

data class TokenDetails(
    val token: String,
    val expiresIn: Long,
    val username: String,
    val roles: List<String>
)

data class TokenPair(
    val accessToken: TokenDetails,
    val refreshToken: TokenDetails
)