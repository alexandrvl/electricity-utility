package com.electricity.utility.application.authentication.service

import com.electricity.utility.infrastructure.security.token.TokenService
import com.electricity.utility.interfaces.authentication.dto.AuthRequest
import com.electricity.utility.interfaces.authentication.dto.AuthResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val authenticationManager: AuthenticationManager,
    private val tokenService: TokenService
) {

    fun authenticate(request: AuthRequest): AuthResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        SecurityContextHolder.getContext().authentication = authentication

        val tokenPair = tokenService.generateTokenPair(authentication)
        return AuthResponse(
            accessToken = tokenPair.accessToken.token,
            refreshToken = tokenPair.refreshToken.token,
            expiresIn = tokenPair.accessToken.expiresIn,
            username = tokenPair.accessToken.username,
            roles = tokenPair.accessToken.roles
        )
    }

    fun refreshToken(token: String): AuthResponse {
        require(tokenService.validateToken(token)) { "Invalid refresh token" }

        val authentication = tokenService.getAuthentication(token)
        val tokenPair = tokenService.generateTokenPair(authentication)

        return AuthResponse(
            accessToken = tokenPair.accessToken.token,
            refreshToken = tokenPair.refreshToken.token,
            expiresIn = tokenPair.accessToken.expiresIn,
            username = tokenPair.accessToken.username,
            roles = tokenPair.accessToken.roles
        )
    }

    fun validateToken(token: String): Boolean {
        return tokenService.validateToken(token)
    }
}
