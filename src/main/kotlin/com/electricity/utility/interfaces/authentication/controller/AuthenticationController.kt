package com.electricity.utility.interfaces.authentication.controller

import com.electricity.utility.application.authentication.service.AuthenticationService
import com.electricity.utility.interfaces.authentication.dto.AuthRequest
import com.electricity.utility.interfaces.authentication.dto.AuthResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        val response = authenticationService.authenticate(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/refresh")
    fun refresh(@RequestHeader("Authorization") authHeader: String): ResponseEntity<AuthResponse> {
        val token = authHeader.substringAfter("Bearer ")
        val response = authenticationService.refreshToken(token)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/validate")
    fun validateToken(@RequestHeader("Authorization") authHeader: String): ResponseEntity<Map<String, Boolean>> {
        val token = authHeader.substringAfter("Bearer ")
        val isValid = authenticationService.validateToken(token)
        return ResponseEntity.ok(mapOf("valid" to isValid))
    }
}