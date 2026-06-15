package com.bike.auth.dto.request

import com.bike.auth.dto.Role
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class RegisterRequest(
  @field:NotBlank(message = "Email is required")
  val email: String,
  @field:NotBlank(message = "Password is required")
  val password: String,
  @field:NotNull(message = "Role is required")
  val role: Role
)
