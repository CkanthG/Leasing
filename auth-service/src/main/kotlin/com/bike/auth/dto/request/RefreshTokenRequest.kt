package com.bike.auth.dto.request

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequest(
  @field:NotBlank(message = "Refresh Token is required")
  val refreshToken: String,
)