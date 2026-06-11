package com.bike.auth.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtService {
    private val secret =
        "your-super-secure-random-secret-key-256-bit"

    private val key = Keys.hmacShaKeyFor(
        secret.toByteArray()
    )

    fun generateToken(email: String): String {
        return Jwts.builder()
            .subject(email)
            .issuedAt(Date())
            .claim("role", "USER")
            .expiration(Date(System.currentTimeMillis() + 86400000))
            .signWith(
                key
            )
            .compact()
    }

    fun extractEmailFromToken(token: String): String {
        return extractAllClaimsFromToken(token).subject
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = extractAllClaimsFromToken(token)

            !claims.expiration.before(Date())
        } catch (ex: Exception) {
            false
        }
    }

    fun extractAllClaimsFromToken(token: String): Claims {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
    }
}