package com.bike.auth.controller

import com.bike.auth.dto.request.LoginRequest
import com.bike.auth.dto.response.LoginResponse
import com.bike.auth.dto.request.RefreshTokenRequest
import com.bike.auth.dto.response.RefreshTokenResponse
import com.bike.auth.dto.request.RegisterRequest
import com.bike.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
  private val authService: AuthService
) {

  @PostMapping("/login")
  fun loginRequest(@Valid @RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
    return ResponseEntity.ok(authService.login(request))
  }

  @PostMapping("/register")
  fun registerRequest(@Valid @RequestBody request: RegisterRequest): ResponseEntity<Void> {
    authService.register(request)
    return ResponseEntity(HttpStatus.CREATED)
  }

  @PostMapping("/refresh")
  fun refreshToken(@Valid @RequestBody request: RefreshTokenRequest): ResponseEntity<RefreshTokenResponse> {
    return ResponseEntity.ok(authService.refreshToken(request));
  }
}
