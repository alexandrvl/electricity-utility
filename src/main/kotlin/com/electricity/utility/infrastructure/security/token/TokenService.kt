package com.electricity.utility.infrastructure.security.token

import org.springframework.security.core.Authentication

interface TokenService {
    fun generateAccessToken(authentication: Authentication): TokenDetails
    fun generateRefreshToken(authentication: Authentication): TokenDetails
    fun generateTokenPair(authentication: Authentication): TokenPair
    fun validateToken(token: String): Boolean
    fun getAuthentication(token: String): Authentication
    fun getUsername(token: String): String
    fun getRoles(token: String): List<String>
}