package com.bike.notification.kafka

import com.bike.notification.dto.NotificationStatus
import com.bike.notification.entity.NotificationEntity
import com.bike.notification.event.NotificationEvent
import com.bike.notification.repository.NotificationRepository
import com.bike.notification.service.EmailService
import com.bike.notification.service.NotificationService
import org.slf4j.MDC
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class NotificationConsumer(
  private val notificationService: NotificationService,
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
    notificationService.process(event)

    emailService.sendEmail(event)
  }
}