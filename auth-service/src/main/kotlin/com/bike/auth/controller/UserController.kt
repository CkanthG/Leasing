package com.bike.auth.controller

import com.bike.auth.dto.response.UserResponse
import com.bike.auth.service.UserService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
  private val userService: UserService
) {

  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
  fun getAllUsers():ResponseEntity<List<UserResponse>> {
    val users = userService.allUsers()
    return ResponseEntity.ok().body(users)
  }

  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @GetMapping("/{userId}")
  fun getUserById(
    @PathVariable userId: Long
  ): ResponseEntity<UserResponse> {
    return ResponseEntity.ok().body(userService.userById(userId))
  }
}