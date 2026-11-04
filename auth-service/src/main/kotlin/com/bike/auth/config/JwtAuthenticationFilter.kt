package com.bike.auth.config

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

  override fun shouldNotFilter(request: HttpServletRequest): Boolean {
    val path = request.servletPath.ifBlank {
      request.requestURI.removePrefix(request.contextPath)
    }
    return path == "/api/v1/auth" || path.startsWith("/api/v1/auth/")
  }

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    val authHeader = request.getHeader("Authorization")

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response)
      return
    }

    val token = authHeader.substring(7)

    if (SecurityContextHolder.getContext().authentication == null && jwtService.validateToken(token)) {
      val email = jwtService.extractEmailFromToken(token)
      val userDetails = userDetailsService.loadUserByUsername(email)
      val authToken = UsernamePasswordAuthenticationToken(
        userDetails,
        null,
        userDetails.authorities
      )

      SecurityContextHolder.getContext().authentication = authToken
    }

    filterChain.doFilter(request, response)
  }
}