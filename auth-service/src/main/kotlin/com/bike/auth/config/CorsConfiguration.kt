package com.bike.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Component
class CorsConfiguration {

  @Bean
  fun corsConfigurationSource():
    UrlBasedCorsConfigurationSource {

    val configuration =
      CorsConfiguration()

    configuration.allowedOrigins =
      listOf(
        "http://localhost:4200"
      )

    configuration.allowedMethods =
      listOf("*")

    configuration.allowedHeaders =
      listOf("*")

    val source = UrlBasedCorsConfigurationSource()

    source.registerCorsConfiguration(
      "/**",
      configuration
    )

    return source
  }
}