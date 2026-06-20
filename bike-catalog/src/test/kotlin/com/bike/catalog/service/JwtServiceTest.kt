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
    val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzQGdtYWlsLmNvbSIsImlhdCI6MTc4MTgyMzkyNywicm9sZSI6IlVTRVIiLCJleHAiOjE3ODE5MTAzMjd9.KQB98dcudMzEGh2hUkg42qwTVzcScX1PvUjyUk--Qlo"

    val validation = service.validateToken(token)
    assertThat(validation).isTrue

    val username = service.extractUserName(token)
    assertThat(username).isEqualTo("s@gmail.com")

    val roles = service.extractRoles(token)
    assertThat(roles).hasSize(4)
  }

}