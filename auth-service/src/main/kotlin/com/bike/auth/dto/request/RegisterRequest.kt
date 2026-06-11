package com.bike.auth.dto.request

import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
  @NotBlank(message = "Email is required")
  val email: String,
  @NotBlank(message = "Password is required")
  val password: String
)
