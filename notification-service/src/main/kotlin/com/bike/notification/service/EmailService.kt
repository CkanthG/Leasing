package com.bike.notification.service

import com.bike.notification.event.NotificationEvent
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender

class EmailService(
  private val mailSender: JavaMailSender
) {

  fun sendEmail(notification: NotificationEvent) {
    val mail = SimpleMailMessage()

    mail.setTo(notification.email)

    mail.subject = notification.subject

    mail.text = notification.message

    mailSender.send(mail)
  }
}