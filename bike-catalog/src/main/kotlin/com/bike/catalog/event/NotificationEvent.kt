package com.bike.notification.event

import java.time.LocalDateTime
import java.util.UUID

data class NotificationEvent(
  val eventId: String,
  val leaseId: UUID,
  val userId: Long,
  val email: String,
  val eventType: String,
  val subject: String,
  val message: String,
  val createdAt: LocalDateTime,
  var traceId: String? = null,
  val leaseTenure: Long
)