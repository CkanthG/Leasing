package com.bike.catalog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
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
@EnableMethodSecurity
class SecurityConfig {

  @Bean
  fun securityFilterChain(
    http: HttpSecurity,
    jwtAuthenticationFilter: JwtAuthenticationFilter
  ): SecurityFilterChain {

    return http
      .csrf { it.disable() }
      .cors {  }
      .formLogin { it.disable() }
      .httpBasic { it.disable() }
      .sessionManagement {
        it.sessionCreationPolicy(
          SessionCreationPolicy.STATELESS
        )
      }
      .authorizeHttpRequests {
        it.requestMatchers(
          "/actuator/**"
        ).permitAll()

        it.anyRequest().authenticated()
      }
      .addFilterBefore(
        jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter::class.java
      )
      .build()
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