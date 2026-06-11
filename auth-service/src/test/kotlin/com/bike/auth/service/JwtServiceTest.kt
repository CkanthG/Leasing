package com.bike.auth.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class JwtServiceTest {

  @InjectMocks
  private lateinit var service: JwtService

  @Test
  fun generateToken_extractEmailFromToken_validateToken_extractAllClaimsFromToken() {
    val response = service.generateToken("s@gmail.com")
    assertThat(response).isNotNull()

    val extractEmail = service.extractEmailFromToken(response)
    assertThat(extractEmail).isEqualTo("s@gmail.com")

    val validation = service.validateToken(response)
    assertThat(validation).isTrue

    val extractAllClaims = service.extractAllClaimsFromToken(response)
    assertThat(extractAllClaims).isNotNull
  }
}
