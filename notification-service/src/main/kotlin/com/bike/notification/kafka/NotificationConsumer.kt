package com.bike.notification.kafka

import com.bike.notification.dto.NotificationStatus
import com.bike.notification.entity.NotificationEntity
import com.bike.notification.event.NotificationEvent
import com.bike.notification.repository.NotificationRepository
import com.bike.notification.service.EmailService
import org.slf4j.MDC
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class NotificationConsumer(
  private val notificationRepository: NotificationRepository,
  private val emailService: EmailService
) {

  @KafkaListener(
    topics = ["bike-lease-notification-topic"],
    groupId = "notification-group"
  )
  fun consume(
    event: NotificationEvent
  ) {
    MDC.put("traceId", event.traceId)
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
      failureReason = ""
    )
    notificationRepository.save(
     notification
    )

    emailService.sendEmail(event)
  }
}