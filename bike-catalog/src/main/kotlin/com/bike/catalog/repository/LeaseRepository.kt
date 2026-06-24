package com.bike.catalog.repository

import com.bike.catalog.entity.Lease
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface LeaseRepository: JpaRepository<Lease, UUID> {
}