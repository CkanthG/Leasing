package com.bike.catalog.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JwtService(
  @param:Value("\${jwt.secret}")
  private val secret: String
) {

  private val key = Keys.hmacShaKeyFor(secret.toByteArray())

  fun extractUserName(token: String): String {

    return Jwts.parser()
      .verifyWith(key)
      .build()
      .parseSignedClaims(token)
      .payload
      .subject
  }

  fun extractRoles(token: String): String {
    val claims = Jwts.parser()
      .verifyWith(key)
      .build()
      .parseSignedClaims(token)
      .payload
    return claims["role"] as String
  }

  fun validateToken(token: String): Boolean {

    try {
      Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
      return true
    } catch (ex: Exception) {
      return false
    }
  }

}