package com.electricity.utility.interfaces.authentication.controller

import com.electricity.utility.application.authentication.service.AuthenticationService
import com.electricity.utility.config.TestConfig
import com.electricity.utility.config.TestSecurityConfig
import com.electricity.utility.interfaces.authentication.dto.AuthRequest
import com.electricity.utility.interfaces.authentication.dto.AuthResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@WebMvcTest(AuthenticationController::class)
@Import(TestConfig::class, TestSecurityConfig::class)
class AuthenticationControllerTest {

    @MockkBean
    private lateinit var tokenService: com.electricity.utility.infrastructure.security.token.TokenService

    init {
        println("[DEBUG_LOG] Initializing AuthenticationControllerTest")
    }

    @MockkBean
    private lateinit var userDetailsService: UserDetailsService

    @MockkBean
    private lateinit var authenticationManager: org.springframework.security.authentication.AuthenticationManager

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var authenticationService: AuthenticationService

    @BeforeEach
    fun setup() {
        try {
            println("[DEBUG_LOG] Setting up test mocks")

            // Setup TokenService mock
            every { 
                tokenService.validateToken(any()) 
            } returns true

            every { 
                tokenService.getAuthentication(any()) 
            } returns org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                "testuser",
                "password",
                listOf(org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"))
            )

            every { 
                tokenService.generateTokenPair(any()) 
            } returns com.electricity.utility.infrastructure.security.token.TokenPair(
                com.electricity.utility.infrastructure.security.token.TokenDetails(
                    token = "test.access.token",
                    expiresIn = 3600,
                    username = "admin",
                    roles = listOf("ROLE_ADMIN", "ROLE_USER")
                ),
                com.electricity.utility.infrastructure.security.token.TokenDetails(
                    token = "test.refresh.token",
                    expiresIn = 7200,
                    username = "admin",
                    roles = listOf("ROLE_ADMIN", "ROLE_USER")
                )
            )

            // Setup UserDetailsService mock
            every { 
                userDetailsService.loadUserByUsername(any()) 
            } returns org.springframework.security.core.userdetails.User
                .withUsername("testuser")
                .password("{noop}password")
                .roles("USER")
                .build()

            // Setup AuthenticationService mock
            val validResponse = AuthResponse(
                accessToken = "test.access.token",
                refreshToken = "test.refresh.token",
                tokenType = "Bearer",
                expiresIn = 3600,
                username = "admin",
                roles = listOf("ROLE_ADMIN", "ROLE_USER")
            )

            every { 
                authenticationService.authenticate(any()) 
            } answers { 
                if (firstArg<AuthRequest>().password == "admin123") validResponse 
                else throw org.springframework.security.authentication.BadCredentialsException("Invalid credentials")
            }

            every { 
                authenticationService.validateToken(any()) 
            } answers { 
                firstArg<String>() == "test.access.token"
            }

            every { 
                authenticationService.refreshToken(any()) 
            } answers { 
                if (firstArg<String>() == "test.access.token") validResponse
                else throw IllegalArgumentException("Invalid token")
            }

            println("[DEBUG_LOG] Test mocks setup completed successfully")
        } catch (e: Exception) {
            println("[DEBUG_LOG] Error setting up test mocks: ${e.message}")
            throw e
        }
    }

    @Test
    @WithMockUser(username = "testuser")
    fun `should authenticate user with valid credentials`() {
        val request = AuthRequest("admin", "admin123")

        val result = mockMvc.perform(post("/api/v1/auth/login")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists())
            .andExpect(jsonPath("$.username").value("admin"))
            .andExpect(jsonPath("$.roles").isArray)
            .andReturn()

        val token = objectMapper.readTree(result.response.contentAsString)
            .get("accessToken").asText()

        // Test token validation
        mockMvc.perform(get("/api/v1/auth/validate")
            .header("Authorization", "Bearer $token"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.valid").value(true))

        // Test token refresh
        mockMvc.perform(post("/api/v1/auth/refresh")
            .with(csrf())
            .header("Authorization", "Bearer $token"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists())
    }

    @Test
    @WithMockUser(username = "testuser")
    fun `should reject authentication with invalid credentials`() {
        val request = AuthRequest("admin", "wrongpassword")

        mockMvc.perform(post("/api/v1/auth/login")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(username = "testuser")
    fun `should reject invalid token`() {
        mockMvc.perform(get("/api/v1/auth/validate")
            .header("Authorization", "Bearer invalid.token.here"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.valid").value(false))
    }
}
