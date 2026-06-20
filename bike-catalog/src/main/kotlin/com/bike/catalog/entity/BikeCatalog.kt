package com.bike.catalog.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class BikeCatalog(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,
  val brand: String,
  val model: String,
  val variant: String,
  val engineCc: String,
  val price: Double,
  val leaseAmount: Double,
  val leaseTenure: Long,
  val mileage: Double,
  val images: ByteArray,
  val availabilityStatus: Boolean,
  val insuranceDetails: String
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as BikeCatalog

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
    var result = id.hashCode()
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