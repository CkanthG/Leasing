package com.bike.catalog.entity

import com.bike.catalog.dto.request.EventType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Lease(
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  val id: UUID?,
  val bike_catalog_id: Long,
  val leaseId: UUID,
  val eventId: UUID,
  val userId: Long,
  val email: String,
  @Enumerated(EnumType.STRING)
  val eventType: EventType,
  val subject: String,
  val createdAt: LocalDateTime
)
