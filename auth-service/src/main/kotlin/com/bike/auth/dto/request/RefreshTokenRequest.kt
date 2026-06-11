package com.bike.auth.dto.request

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequest(
  @NotBlank(message = "Refresh Token is required")
  val refreshToken: String,
)