package com.bike.catalog.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JwtServiceTest {

  private lateinit var service: JwtService

  @BeforeEach
  fun setup() {
    service = JwtService(
      "my-test-secret-key-my-test-secret-key"
    )
  }

  @Test
  fun validateToken__extractUserName__extractRoles() {
    // dummy token to test
    val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzQGdtYWlsLmNvbSIsImlhdCI6MTc4MjM3OTQzOCwicm9sZSI6IlVTRVIiLCJleHAiOjE3ODI0NjU4Mzh9.JSESFULUWzRk9czfULcFpZhh3aRswdvk2nL22Ep8bZs"

    val validation = service.validateToken(token)
    assertThat(validation).isTrue

    val username = service.extractUserName(token)
    assertThat(username).isEqualTo("s@gmail.com")

    val roles = service.extractRoles(token)
    assertThat(roles).hasSize(4)
  }

}