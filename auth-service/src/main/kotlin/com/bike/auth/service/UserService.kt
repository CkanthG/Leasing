package com.bike.auth.service

import com.bike.auth.dto.response.UserResponse
import com.bike.auth.exception.UserNotFoundException
import com.bike.auth.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
  private val userRepository: UserRepository
) {

  fun allUsers(): List<UserResponse> {
    return userRepository.findAll().stream().map {
      UserResponse(
        id = it.id,
        email = it.email,
        role = it.role,
        enabled = it.enabled,
        createdAt = it.createdAt
      )
    }.toList()
  }

  fun userById(id: Long): UserResponse {
    val user =  userRepository.findById(id).orElseGet {
      throw UserNotFoundException("User not found with id: $id")
    }
    return UserResponse(
      id = user.id,
      email = user.email,
      role = user.role,
      enabled = user.enabled,
      createdAt = user.createdAt
    )
  }
}