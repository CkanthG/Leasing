package com.bike.auth.dto.response

data class LoginResponse(
  val accessToken: String,
  val refreshToken: String,
)
