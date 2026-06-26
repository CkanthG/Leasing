package com.bike.notification.service

import com.bike.notification.dto.NotificationStatus
import com.bike.notification.entity.NotificationEntity
import com.bike.notification.event.NotificationEvent
import com.bike.notification.repository.NotificationRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class NotificationService(
  private val notificationRepository: NotificationRepository
) {

  fun process(event: NotificationEvent) {
    val notification = NotificationEntity(
      id = null,
      eventId = event.eventId,
      leaseId = event.leaseId,
      userId = event.userId,
      email = event.email,
      eventType = event.eventType,
      subject = event.subject,
      message = event.message,
      createdAt = event.createdAt,
      status = NotificationStatus.PENDING,
      retryCount = 0,
      sentAt = LocalDateTime.now(),
      failureReason = "",
      leaseTenure = event.leaseTenure
    )

    notificationRepository.save(notification)
  }
}