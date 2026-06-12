package com.bike.auth.dto.request

import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
  @field:NotBlank(message = "Email is required")
  val email: String,
  @field:NotBlank(message = "Password is required")
  val password: String
)
