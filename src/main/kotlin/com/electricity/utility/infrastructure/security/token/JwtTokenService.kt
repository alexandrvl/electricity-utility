package com.electricity.utility.infrastructure.security.token

import com.electricity.utility.infrastructure.security.config.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtTokenService(
    private val jwtProperties: JwtProperties
) : TokenService {

    private val secretKey = Keys.hmacShaKeyFor(
        Base64.getEncoder().encode(jwtProperties.secret.toByteArray())
    )

    override fun generateAccessToken(authentication: Authentication): TokenDetails {
        return generateToken(authentication, jwtProperties.accessTokenExpiration)
    }

    override fun generateRefreshToken(authentication: Authentication): TokenDetails {
        return generateToken(authentication, jwtProperties.refreshTokenExpiration)
    }

    override fun generateTokenPair(authentication: Authentication): TokenPair {
        return TokenPair(
            accessToken = generateAccessToken(authentication),
            refreshToken = generateRefreshToken(authentication)
        )
    }

    override fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: JwtException) {
            false
        }
    }

    override fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token)
        val authorities = getRoles(token).map { SimpleGrantedAuthority(it) }
        return UsernamePasswordAuthenticationToken(claims.subject, null, authorities)
    }

    override fun getUsername(token: String): String {
        return getClaims(token).subject
    }

    override fun getRoles(token: String): List<String> {
        return getClaims(token).get("roles", List::class.java) as List<String>
    }

    private fun generateToken(authentication: Authentication, expirationTime: Long): TokenDetails {
        val now = Date()
        val validity = Date(now.time + expirationTime * 1000)

        val roles = authentication.authorities.map { it.authority }
        val token = Jwts.builder()
            .setSubject(authentication.name)
            .setIssuedAt(now)
            .setExpiration(validity)
            .setIssuer(jwtProperties.issuer)
            .setAudience(jwtProperties.audience)
            .claim("roles", roles)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact()

        return TokenDetails(
            token = token,
            expiresIn = expirationTime,
            username = authentication.name,
            roles = roles
        )
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }
}