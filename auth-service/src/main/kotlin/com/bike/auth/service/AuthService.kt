package com.bike.auth.service

import com.bike.auth.exception.UserNotFoundException
import com.bike.auth.dto.request.LoginRequest
import com.bike.auth.dto.response.LoginResponse
import com.bike.auth.dto.request.RefreshTokenRequest
import com.bike.auth.dto.response.RefreshTokenResponse
import com.bike.auth.dto.request.RegisterRequest
import com.bike.auth.dto.Role
import com.bike.auth.entity.RefreshTokenEntity
import com.bike.auth.entity.UserEntity
import com.bike.auth.exception.InvalidCredentialsException
import com.bike.auth.exception.TokenException
import com.bike.auth.exception.UserFoundException
import com.bike.auth.repository.RefreshTokenRepository
import com.bike.auth.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class AuthService(
  private val userRepository: UserRepository,
  private val passwordEncoder: PasswordEncoder,
  private val jwtService: JwtService,
  private val refreshTokenRepository: RefreshTokenRepository
) {

  fun login(loginRequest: LoginRequest): LoginResponse {
    val user = userRepository.findByEmail(loginRequest.email)
      ?: throw UserNotFoundException("User ${loginRequest.email} not found")

    val valid = passwordEncoder.matches(loginRequest.password, user.password)

    if (!valid) {
      throw InvalidCredentialsException("Invalid credentials")
    }

    val accessToken = jwtService.generateToken(user.email)
    val refreshToken = UUID.randomUUID().toString()

    refreshTokenRepository.save(
      RefreshTokenEntity(
        token = refreshToken,
        expiresAt = LocalDateTime.now().plusDays(7),
        user = user
      )
    )

    return LoginResponse(
      accessToken = accessToken,
      refreshToken = refreshToken,
    )
  }

  fun register(request: RegisterRequest) {
    if (userRepository.findByEmail(request.email) != null) {
      throw UserFoundException("User ${request.email} found, cannot create user with email ${request.email}")
    }

    val user = UserEntity(
      email = request.email,
      password = passwordEncoder.encode(request.password),
      role = Role.USER,
    )

    userRepository.save(user)
  }

  fun refreshToken(request: RefreshTokenRequest): RefreshTokenResponse {
    val refreshToken = refreshTokenRepository.findByToken(token = request.refreshToken)
      ?: throw TokenException("Refresh Token ${request.refreshToken} not found")

    if (refreshToken.revoked) {
      throw TokenException("Token revoked")
    }

    if (refreshToken.expiresAt.isBefore(LocalDateTime.now())) {
      throw TokenException("Token expired")
    }

    val accessToken = jwtService.generateToken(refreshToken.user.email)

    return RefreshTokenResponse(accessToken);
  }
}
