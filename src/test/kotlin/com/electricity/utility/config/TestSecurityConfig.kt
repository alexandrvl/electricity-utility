package com.electricity.utility.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@TestConfiguration
@EnableWebSecurity
class TestSecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/v1/auth/**").permitAll()
                    .anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint { request, response, exception ->
                    response.sendError(org.springframework.http.HttpStatus.UNAUTHORIZED.value(), exception.message)
                }
            }

        return http.build()
    }

    @Bean
    fun userDetailsService(): org.springframework.security.core.userdetails.UserDetailsService {
        val user = org.springframework.security.core.userdetails.User.builder()
            .username("testuser")
            .password("{noop}password")
            .roles("USER")
            .build()
        return org.springframework.security.provisioning.InMemoryUserDetailsManager(user)
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration): org.springframework.security.authentication.AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}
