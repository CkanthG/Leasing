package com.bike.catalog.dto.request

data class BikeCatalogSearchRequest(
  val bikeCatalogId: Long? = null,
  val brand: String? = null,
  val model: String? = null,
  val price: Double? = null,
  val mileage: Double? = null,
  val available: Boolean? = null,
  val insurance: String? = null
)
