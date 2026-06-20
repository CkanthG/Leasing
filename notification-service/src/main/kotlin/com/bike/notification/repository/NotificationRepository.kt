package com.bike.notification.repository

import com.bike.notification.entity.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface NotificationRepository: JpaRepository<NotificationEntity, UUID> {
}