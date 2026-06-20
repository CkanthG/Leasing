package com.bike.catalog.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile

data class BikeCatalogRequest(
  var id: Long? = null,
  @field:NotBlank(message = "Brand cannot be blank")
  val brand: String,
  @field:NotBlank(message = "Model cannot be blank")
  val model: String,
  @field:NotBlank(message = "Variant cannot be blank")
  val variant: String,
  @field:NotBlank(message = "Engine CC cannot be blank")
  val engineCc: String,
  @field:NotNull(message = "Price cannot be null or empty")
  val price: Double,
  @field:NotNull(message = "Lease Amount cannot be null or empty")
  val leaseAmount: Double,
  @field:NotNull(message = "Lease Tenure cannot be null or empty")
  val leaseTenure: Long,
  @field:NotNull(message = "Mileage cannot be null or empty")
  val mileage: Double,
  val images: MultipartFile?,
  @field:NotNull(message = "Availability cannot be null or empty")
  val availabilityStatus: Boolean,
  val insuranceDetails: String
) {
}