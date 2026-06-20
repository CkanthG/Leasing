package com.bike.catalog.config

import com.bike.catalog.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
  private val jwtService: JwtService
): OncePerRequestFilter() {

  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ) {
    val authHeader = request.getHeader("Authorization")

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      val token = authHeader.substring(7)

      try {
        if (jwtService.validateToken(token)) {

          val username = jwtService.extractUserName(token)
          val role = jwtService.extractRoles(token)

          val auth = UsernamePasswordAuthenticationToken(
            username,
            null,
            listOf(SimpleGrantedAuthority("ROLE_$role"))
          )

          SecurityContextHolder.getContext().authentication = auth
        }
      } catch (ex: Exception) {
        SecurityContextHolder.clearContext()

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token")
      }
    }

    filterChain.doFilter(request, response)
  }

}