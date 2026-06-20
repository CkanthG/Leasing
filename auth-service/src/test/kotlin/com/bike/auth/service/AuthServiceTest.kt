package com.bike.auth.service

import com.bike.auth.dto.Role
import com.bike.auth.dto.request.LoginRequest
import com.bike.auth.dto.request.RefreshTokenRequest
import com.bike.auth.dto.request.RegisterRequest
import com.bike.auth.entity.RefreshTokenEntity
import com.bike.auth.entity.UserEntity
import com.bike.auth.exception.InvalidCredentialsException
import com.bike.auth.exception.TokenException
import com.bike.auth.exception.UserFoundException
import com.bike.auth.exception.UserNotFoundException
import com.bike.auth.repository.RefreshTokenRepository
import com.bike.auth.repository.UserRepository
import org.apache.logging.log4j.util.Strings
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class AuthServiceTest {

  @InjectMocks
  lateinit var authService: AuthService
  @Mock
  lateinit var userRepository: UserRepository
  @Mock
  lateinit var passwordEncoder: PasswordEncoder
  @Mock
  lateinit var jwtService: JwtService
  @Mock
  lateinit var refreshTokenRepository: RefreshTokenRepository

  var user: UserEntity? = null
  var token: String? = null

  @BeforeEach
  fun setup() {
    user = UserEntity(email = "s@gmail.com", password = passwordEncoder.encode("user"), role = Role.USER)
    token = Strings.repeat("ruywefyfwxycyxca", 12)
  }

  @Test
  fun `user should login successfully`() {
    val user = UserEntity(email = "s@gmail.com", password = passwordEncoder.encode("user"), role = Role.USER)
    val loginRequest = LoginRequest("s@gmail.com", "user")


    `when`(userRepository.findByEmail(loginRequest.email)).thenReturn(user);
    `when`(jwtService.generateToken(loginRequest.email, user.role.name)).thenReturn(token)
    `when`(passwordEncoder.matches(loginRequest.password, user.password)).thenReturn(true)

    val response = authService.login(loginRequest)

    assertThat(response).isNotNull()
    assertThat(response.accessToken).isEqualTo(token)
  }

  @Test
  fun `user not found when logging in`() {
    val loginRequest = LoginRequest("s@gmail.com", "user")

    assertThrows(UserNotFoundException::class.java) {
      authService.login(loginRequest)
    }
  }

  @Test
  fun `user token expired`() {
    val user = UserEntity(email = "s@gmail.com", password = passwordEncoder.encode("user"), role = Role.USER)
    val loginRequest = LoginRequest("s@gmail.com", "user")

    `when`(userRepository.findByEmail(loginRequest.email)).thenReturn(user);

    assertThrows(InvalidCredentialsException::class.java) {
      authService.login(loginRequest)
    }
  }

  @Test
  fun `user should successfully register into system`() {
    val registerRequest = RegisterRequest("sg@gmail.com", "password", Role.USER)

    `when`(passwordEncoder.encode(registerRequest.password)).thenReturn(registerRequest.password)

    authService.register(registerRequest)

    verify(passwordEncoder, times(1)).encode(registerRequest.password)
  }

  @Test
  fun `user registration failed due to duplicate email`() {
    val registerRequest = RegisterRequest("s@gmail.com", "password", Role.USER)

    `when`(userRepository.findByEmail(registerRequest.email)).thenReturn(user)

    assertThrows(UserFoundException::class.java) {
      authService.register(registerRequest)
    }
  }

  @Test
  fun `user should refresh token`() {
    val refreshTokenUUID = UUID.randomUUID().toString()
    val refreshTokenEntity = RefreshTokenEntity(token = refreshTokenUUID, expiresAt = LocalDateTime.now().plusHours(1), user = user!!)
    val refreshTokenRequest = RefreshTokenRequest(refreshTokenUUID)

    `when`(refreshTokenRepository.findByToken(refreshTokenUUID)).thenReturn(refreshTokenEntity)
    `when`(jwtService.generateToken(refreshTokenEntity.user.email, refreshTokenEntity.user.role.name)).thenReturn(token)

    val refreshToken = authService.refreshToken(request = refreshTokenRequest)

    assertThat(refreshToken).isNotNull()
    assertThat(refreshToken.accessToken).isEqualTo(token)
  }

  @Test
  fun `user refresh token id not found when refreshing token`() {
    val refreshTokenUUID = UUID.randomUUID().toString()
    val refreshTokenRequest = RefreshTokenRequest(refreshTokenUUID)

    assertThrows(TokenException::class.java) {
      authService.refreshToken(refreshTokenRequest)
    }
  }

  @Test
  fun `user refresh token expired`() {
    val refreshTokenUUID = UUID.randomUUID().toString()
    val refreshTokenRequest = RefreshTokenRequest(refreshTokenUUID)
    val refreshTokenEntity = RefreshTokenEntity(token = refreshTokenUUID, expiresAt = LocalDateTime.now().minusHours(1), user = user!!)
    `when`(refreshTokenRepository.findByToken(refreshTokenUUID)).thenReturn(refreshTokenEntity)

    assertThrows(TokenException::class.java) {
      authService.refreshToken(refreshTokenRequest)
    }
  }

  @Test
  fun `user refresh token revoked`() {
    val refreshTokenUUID = UUID.randomUUID().toString()
    val refreshTokenRequest = RefreshTokenRequest(refreshTokenUUID)
    val refreshTokenEntity = RefreshTokenEntity(token = refreshTokenUUID, expiresAt = LocalDateTime.now().minusHours(1), user = user!!, revoked = true)
    `when`(refreshTokenRepository.findByToken(refreshTokenUUID)).thenReturn(refreshTokenEntity)

    assertThrows(TokenException::class.java) {
      authService.refreshToken(refreshTokenRequest)
    }
  }
}
