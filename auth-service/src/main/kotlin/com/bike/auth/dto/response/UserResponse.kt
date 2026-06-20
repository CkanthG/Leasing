package com.bike.auth.dto.response

import com.bike.auth.dto.Role
import java.time.LocalDateTime

data class UserResponse(
  val id: Long,
  val email: String,
  val role: Role,
  val enabled: Boolean,
  val createdAt: LocalDateTime
)
