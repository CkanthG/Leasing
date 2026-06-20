package com.bike.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
  private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .csrf {
        it.disable()
      }
      .cors {

      }
      .sessionManagement {
        it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      }
      .authorizeHttpRequests {
        it.requestMatchers("/api/v1/auth/**").permitAll()

        it.anyRequest().authenticated()
      }
      .addFilterBefore(
        jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter::class.java
      )
    return http.build()
  }

  @Bean
  fun corsConfigurationSource(): CorsConfigurationSource {

    val configuration = CorsConfiguration()

    configuration.allowedOrigins = listOf(
      "http://localhost:4200",
	  "http://172.26.152.45:4200"
    )

    configuration.allowedMethods = listOf(
      "GET",
      "POST",
      "PUT",
      "DELETE",
      "PATCH",
      "OPTIONS"
    )

    configuration.allowedHeaders = listOf("*")

    configuration.allowCredentials = true

    val source = UrlBasedCorsConfigurationSource()

    source.registerCorsConfiguration(
      "/**",
      configuration
    )

    return source
  }
}