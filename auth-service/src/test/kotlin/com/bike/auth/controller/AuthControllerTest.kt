package com.bike.auth.controller

import com.bike.auth.dto.response.LoginResponse
import com.bike.auth.dto.response.RefreshTokenResponse
import com.bike.auth.repository.RefreshTokenRepository
import com.bike.auth.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tools.jackson.databind.ObjectMapper
import kotlin.random.Random

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

  @Autowired
  lateinit var mockMvc: MockMvc
  @Autowired
  lateinit var userRepository: UserRepository
  @Autowired
  lateinit var refreshTokenRepository: RefreshTokenRepository

  @AfterEach
  fun cleanUp() {
    refreshTokenRepository.deleteAll()
    userRepository.deleteAll()
  }

  @Test
  fun test_register_login_and_refresh_token() {
    val email = "sg1@gmail.com"
    val password = Random(10000).nextLong()
    mockMvc
      .perform(
        post("/api/v1/auth/register")
          .content(
            String.format("""
          {
            "email" : "%s",
            "password" : "%d"
          }
        """, email, password).trimIndent()
          ).contentType(MediaType.APPLICATION_JSON)
      ).andExpect(status().isCreated)

    val result = mockMvc.perform(
      post("/api/v1/auth/login")
      .content(
        String.format("""
        {
            "email" : "%s",
            "password" : "%d"
          }
        """, email, password).trimIndent())
        .contentType(MediaType.APPLICATION_JSON)
      ).andExpect(status().isOk)
      .andReturn()

    val objectMapper = ObjectMapper()
    val res: LoginResponse =
      objectMapper.readValue(result.response.contentAsString, LoginResponse::class.java)

    assertThat(res.accessToken).isNotBlank
    assertThat(res.refreshToken).isNotBlank

    val refrehTokenRes = mockMvc.perform(
      post("/api/v1/auth/refresh")
        .content(
          String.format("""
            {
              "refreshToken" : "%s"
            }
          """, res.refreshToken).trimIndent()
        ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk)
      .andReturn()

    val refreshTokenResponse : RefreshTokenResponse =
      objectMapper.readValue(refrehTokenRes.response.contentAsString, RefreshTokenResponse::class.java)
    assertThat(refreshTokenResponse.accessToken).isNotBlank
  }

  @Test
  fun registration_failed_without_email() {
    mockMvc.perform(
      post("/api/v1/auth/register")
      .content(
        """
          {
          "email": "",
          "password": "123456"
          }
        """
      ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isBadRequest)
  }

  @Test
  fun registration_failed_if_email_is_null() {
    mockMvc.perform(
      post("/api/v1/auth/register")
      .content(
        """
          {
          "email": null,
          "password": "123456"
          }
        """
      ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isBadRequest)
  }

  @Test
  fun registration_failed_without_password() {
    mockMvc.perform(
      post("/api/v1/auth/register")
      .content(
        """
          {
          "email": "s@gmail.com",
          "password": ""
          }
        """
      ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isBadRequest)
  }

  @Test
  fun registration_failed_if_password_is_null() {
    mockMvc.perform(
      post("/api/v1/auth/register")
      .content(
        """
          {
          "email": "s@gmail.com",
          "password": null
          }
        """
      ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isBadRequest)
  }

  @Test
  fun login_failed_without_email() {
    mockMvc.perform(
      post("/api/v1/auth/login")
      .content(
        """
          {
          "email": "",
          "password": "456789"
          }
        """
      ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isBadRequest)
  }

  @Test
  fun login_failed_without_if_email_is_null() {
    mockMvc.perform(
      post("/api/v1/auth/login")
      .content(
        """
          {
          "email": null,
          "password": "456789"
          }
        """
      ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isBadRequest)
  }

  @Test
  fun login_failed_without_password() {
    mockMvc.perform(
      post("/api/v1/auth/login")
      .content(
        """
          {
          "email": "s@gmail.com",
          "password": ""
          }
        """
      ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isBadRequest)
  }

  @Test
  fun login_failed_without_if_password_is_null() {
    mockMvc.perform(
      post("/api/v1/auth/login")
      .content(
        """
          {
          "email": "s@gmail.com",
          "password": null
          }
        """
      ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isBadRequest)
  }

  @Test
  fun refresh_token_failed_without_refresh_token_id() {
    mockMvc.perform(
      post("/api/v1/auth/refresh")
      .content(
        """
          {
          "refreshToken": ""
          }
        """
      ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isBadRequest)
  }

  @Test
  fun refresh_token_failed_if_refresh_token_id_is_null() {
    mockMvc.perform(
      post("/api/v1/auth/refresh")
      .content(
        """
          {
          "refreshToken": null
          }
        """
      ).contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isBadRequest)
  }
}
