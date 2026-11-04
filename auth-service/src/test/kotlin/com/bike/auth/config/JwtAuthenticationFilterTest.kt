package com.bike.auth.config

import com.bike.auth.service.JwtService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verifyNoInteractions
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.mock.web.MockFilterChain

class JwtAuthenticationFilterTest {

  private val jwtService = Mockito.mock(JwtService::class.java)
  private val userDetailsService = Mockito.mock(UserDetailsService::class.java)
  private val filter = JwtAuthenticationFilter(jwtService, userDetailsService)

  @AfterEach
  fun clearSecurityContext() {
    SecurityContextHolder.clearContext()
  }

  @Test
  fun `public auth requests ignore stale bearer tokens`() {
    val request = MockHttpServletRequest("POST", "/api/v1/auth/login")
    request.addHeader("Authorization", "Bearer expired-token")
    val response = MockHttpServletResponse()
    val chain = MockFilterChain()

    filter.doFilter(request, response, chain)

    assertThat(chain.request).isSameAs(request)
    verifyNoInteractions(jwtService, userDetailsService)
  }

  @Test
  fun `expired bearer tokens do not throw on protected requests`() {
    val request = MockHttpServletRequest("GET", "/api/v1/users/me")
    request.addHeader("Authorization", "Bearer expired-token")
    val response = MockHttpServletResponse()
    val chain = MockFilterChain()
    `when`(jwtService.validateToken("expired-token")).thenReturn(false)

    filter.doFilter(request, response, chain)

    assertThat(chain.request).isSameAs(request)
    verifyNoInteractions(userDetailsService)
  }
}
