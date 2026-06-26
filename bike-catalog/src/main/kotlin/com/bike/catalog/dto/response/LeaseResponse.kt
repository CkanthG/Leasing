package com.bike.catalog.dto.response

import com.bike.catalog.dto.request.EventType
import java.time.LocalDateTime
import java.util.UUID

data class LeaseResponse(
  val leaseId: UUID,
  val eventId: UUID,
  val eventType: EventType,
  val createdAt: LocalDateTime,
  val leaseTenure: Long
)