package com.bike.auth.entity

import com.bike.auth.dto.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(unique = true)
    val email: String,
    val password: String?,
    @Enumerated(EnumType.STRING)
    val role: Role,
    val enabled: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {}
