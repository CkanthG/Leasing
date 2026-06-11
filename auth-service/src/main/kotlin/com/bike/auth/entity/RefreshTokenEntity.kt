package com.bike.auth.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "refresh_tokens")
data class RefreshTokenEntity(

  @Id
  val id: UUID = UUID.randomUUID(),

  val token: String,

  val expiresAt: LocalDateTime,

  val revoked: Boolean = false,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  val user : UserEntity
)
