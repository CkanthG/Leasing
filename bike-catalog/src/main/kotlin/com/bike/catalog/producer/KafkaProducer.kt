package com.bike.catalog.producer

import com.bike.notification.event.NotificationEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(
  private val kafkaTemplate: KafkaTemplate<String, NotificationEvent>
) {

  fun producer(notificationEvent: NotificationEvent) {
    kafkaTemplate.send(
      "bike-lease-notification-topic",
      notificationEvent
    )

    println("Kafka Sent Data to bike-lease-notification-topic")
  }
}