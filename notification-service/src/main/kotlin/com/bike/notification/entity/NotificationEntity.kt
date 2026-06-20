package com.bike.notification.entity

import com.bike.notification.dto.NotificationStatus
import jakarta.persistence.Id
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Enumerated
import jakarta.persistence.EnumType
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "notification")
class NotificationEntity(

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  val id: UUID,

  val eventId: String,

  val leaseId: UUID?,

  val userId: Long?,

  val email: String,

  val eventType: String,

  val subject: String,

  val message: String,

  @Enumerated(EnumType.STRING)
  var status: NotificationStatus,

  var retryCount: Int,

  val createdAt: LocalDateTime,

  var sentAt: LocalDateTime?,

  var failureReason: String?
)