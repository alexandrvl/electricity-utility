package com.electricity.utility.infrastructure.security.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security.jwt")
data class JwtProperties(
    val secret: String = "default_secret_key_which_should_be_replaced_in_production",
    val issuer: String = "electricity.utility",
    val audience: String = "electricity.utility.users",
    val accessTokenExpiration: Long = 3600, // 1 hour in seconds
    val refreshTokenExpiration: Long = 86400 // 24 hours in seconds
)