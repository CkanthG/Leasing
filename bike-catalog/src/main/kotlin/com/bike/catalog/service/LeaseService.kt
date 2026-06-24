package com.bike.catalog.service

import com.bike.catalog.dto.request.EventType
import com.bike.catalog.dto.request.LeaseRequest
import com.bike.catalog.dto.response.LeaseResponse
import com.bike.catalog.entity.Lease
import com.bike.catalog.producer.KafkaProducer
import com.bike.catalog.repository.LeaseRepository
import com.bike.notification.event.NotificationEvent
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class LeaseService(
  private val repository: LeaseRepository,
  private val kafkaProducer: KafkaProducer
) {

  fun applyLease(request: LeaseRequest): LeaseResponse {
    val leaseEntity = toLeaseEntity(request)

    val lease = repository.save(leaseEntity)

    kafkaProducer.producer(
      NotificationEvent(
        eventId = lease.eventId.toString(),
        leaseId = lease.leaseId,
        userId = lease.userId,
        email = lease.email,
        eventType = lease.eventType.toString(),
        subject = lease.subject,
        message = "Your lease request is in Draft state",
        createdAt = lease.createdAt
      )
    )

    return LeaseResponse(
      lease.leaseId,
      lease.eventId,
      lease.eventType,
      lease.createdAt,
    )
  }

  private fun toLeaseEntity(request: LeaseRequest): Lease {
    return Lease(
      null,
      request.bikeCatalogId,
      UUID.randomUUID(),
      UUID.randomUUID(),
      0,
      request.email,
      EventType.DRAFT,
      "Your bike lease request has been submitted",
      LocalDateTime.now()
    )
  }
}