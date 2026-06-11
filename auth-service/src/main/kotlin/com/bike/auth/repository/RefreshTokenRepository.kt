package com.bike.auth.repository

import com.bike.auth.entity.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository: JpaRepository<RefreshTokenEntity, Long> {
  fun findByToken(token: String): RefreshTokenEntity?
}