package com.bike.notification.config

import com.bike.notification.event.NotificationEvent
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@Configuration
class KafkaConsumerConfig(
  private val kafkaProperties: KafkaProperties
) {

  @Bean
  fun consumerFactory(): ConsumerFactory<String, NotificationEvent> {
    return DefaultKafkaConsumerFactory(
      kafkaProperties.buildConsumerProperties()
    )
  }

  @Bean
  fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, NotificationEvent> {
      val factory = ConcurrentKafkaListenerContainerFactory<String, NotificationEvent>()
      factory.consumerFactory = consumerFactory()
      return factory
  }
}