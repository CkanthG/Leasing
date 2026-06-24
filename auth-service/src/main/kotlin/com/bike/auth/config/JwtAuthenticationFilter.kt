package com.bike.auth.config

import com.bike.auth.exception.TokenException
import com.bike.auth.repository.UserRepository
import com.bike.auth.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter

@Service
class JwtAuthenticationFilter(
  private val jwtService: JwtService,
  private val userDetailsService: UserDetailsService,
): OncePerRequestFilter() {

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    val authHeader = request.getHeader("Authorization")

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response)
      return
    }

    val token = authHeader.substring(7)
    val email: String
    if (jwtService.validateToken(token)) {
      email = jwtService.extractEmailFromToken(token)
    } else {
      throw TokenException("Token Expired")
    }

    if (SecurityContextHolder.getContext().authentication == null) {
      val userDetails = userDetailsService.loadUserByUsername(email)

      if (jwtService.validateToken(token)) {
        val authToken = UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.authorities
        )

        SecurityContextHolder.getContext().authentication = authToken
      }

    }

    filterChain.doFilter(request, response)
  }
}