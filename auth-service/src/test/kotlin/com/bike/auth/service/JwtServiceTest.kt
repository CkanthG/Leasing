package com.bike.auth.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class JwtServiceTest {

  private lateinit var service: JwtService

  @BeforeEach
  fun setup() {
    service = JwtService(
      "my-test-secret-key-my-test-secret-key"
    )
  }

  @Test
  fun generateToken_extractEmailFromToken_validateToken_extractAllClaimsFromToken() {
    val response = service.generateToken("s@gmail.com", "USER")
    assertThat(response).isNotNull()

    val extractEmail = service.extractEmailFromToken(response)
    assertThat(extractEmail).isEqualTo("s@gmail.com")

    val validation = service.validateToken(response)
    assertThat(validation).isTrue

    val extractAllClaims = service.extractAllClaimsFromToken(response)
    assertThat(extractAllClaims).isNotNull
  }
}
