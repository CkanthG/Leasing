package com.bike.auth.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val secret: String
) {

    private val key = Keys.hmacShaKeyFor(
        secret.toByteArray()
    )

    fun generateToken(email: String, role: String): String {
        return Jwts.builder()
            .subject(email)
            .issuedAt(Date())
            .claim("role", role)
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