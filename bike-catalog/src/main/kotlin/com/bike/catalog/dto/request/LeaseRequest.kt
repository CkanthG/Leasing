package com.bike.catalog.dto.request

import java.time.LocalDateTime
import java.util.UUID

data class LeaseRequest(
  val bikeCatalogId: Long,
  val email: String,
  val leaseTenure: Long
)
