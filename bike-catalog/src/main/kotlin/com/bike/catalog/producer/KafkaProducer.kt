package com.bike.catalog.producer

import com.bike.notification.event.NotificationEvent
import org.slf4j.MDC
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(
  private val kafkaTemplate: KafkaTemplate<String, NotificationEvent>
) {

  fun producer(notificationEvent: NotificationEvent) {
    val traceId = MDC.get("traceId")
    notificationEvent.traceId = traceId
    kafkaTemplate.send(
      "bike-lease-notification-topic",
      notificationEvent
    )
  }
}