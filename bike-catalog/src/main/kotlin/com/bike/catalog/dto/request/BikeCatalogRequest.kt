package com.bike.catalog.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

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
  val images: ByteArray,
  @field:NotNull(message = "Availability cannot be null or empty")
  val availabilityStatus: Boolean,
  val insuranceDetails: String
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as BikeCatalogRequest

    if (id != other.id) return false
    if (price != other.price) return false
    if (leaseAmount != other.leaseAmount) return false
    if (leaseTenure != other.leaseTenure) return false
    if (mileage != other.mileage) return false
    if (availabilityStatus != other.availabilityStatus) return false
    if (brand != other.brand) return false
    if (model != other.model) return false
    if (variant != other.variant) return false
    if (engineCc != other.engineCc) return false
    if (!images.contentEquals(other.images)) return false
    if (insuranceDetails != other.insuranceDetails) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id?.hashCode() ?: 0
    result = 31 * result + price.hashCode()
    result = 31 * result + leaseAmount.hashCode()
    result = 31 * result + leaseTenure.hashCode()
    result = 31 * result + mileage.hashCode()
    result = 31 * result + availabilityStatus.hashCode()
    result = 31 * result + brand.hashCode()
    result = 31 * result + model.hashCode()
    result = 31 * result + variant.hashCode()
    result = 31 * result + engineCc.hashCode()
    result = 31 * result + images.contentHashCode()
    result = 31 * result + insuranceDetails.hashCode()
    return result
  }
}