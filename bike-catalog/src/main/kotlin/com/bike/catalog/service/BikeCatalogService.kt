package com.bike.catalog.service

import com.bike.catalog.dto.request.BikeCatalogRequest
import com.bike.catalog.dto.request.BikeCatalogSearchRequest
import com.bike.catalog.dto.response.BikeCatalogResponse
import com.bike.catalog.entity.BikeCatalog
import com.bike.catalog.exception.BikeCatalogException
import com.bike.catalog.producer.KafkaProducer
import com.bike.catalog.repository.BikeCatalogRepository
import com.bike.notification.event.NotificationEvent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class BikeCatalogService(
  private val bikeCatalogRepository: BikeCatalogRepository,
  private val kafkaProducer: KafkaProducer
) {

  fun saveBikeCatalog(bikeCatalogRequest: BikeCatalogRequest): BikeCatalogResponse {
    val bikeCatalog = toBikeCatalogEntity(bikeCatalogRequest)

    val bc = bikeCatalogRepository.save(bikeCatalog)
    kafkaProducer.producer(
      NotificationEvent(
        eventId = UUID.randomUUID().toString(),
        leaseId = UUID.randomUUID(),
        userId = 1L,
        email = "gaddojuc@gmail.com",
        eventType = "LEASE_CREATED",
        subject = "Lease Created",
        message = "Your bike lease request has been submitted",
        createdAt = LocalDateTime.now()
      )
    )

    return toResponse(bc)
  }

  private fun toBikeCatalogEntity(bikeCatalogRequest: BikeCatalogRequest): BikeCatalog {
    return BikeCatalog(
      id = bikeCatalogRequest.id ?: 0,
      brand = bikeCatalogRequest.brand,
      model = bikeCatalogRequest.model,
      variant = bikeCatalogRequest.variant,
      engineCc = bikeCatalogRequest.engineCc,
      price = bikeCatalogRequest.price,
      leaseAmount = bikeCatalogRequest.leaseAmount,
      leaseTenure = bikeCatalogRequest.leaseTenure,
      mileage = bikeCatalogRequest.mileage,
      images = bikeCatalogRequest.images?.bytes ?: "test".toByteArray(),
      availabilityStatus = bikeCatalogRequest.availabilityStatus,
      insuranceDetails = bikeCatalogRequest.insuranceDetails
    )
  }

  fun getBikeCatalogById(id: Long): BikeCatalogResponse {
    val bc = bikeCatalog(id)

    return toResponse(bc)
  }

  private fun bikeCatalog(id: Long): BikeCatalog = bikeCatalogRepository.findById(id).orElseThrow {
    throw BikeCatalogException("Bike catalog not found")
  }

  private fun toResponse(bc: BikeCatalog): BikeCatalogResponse = BikeCatalogResponse(
    id = bc.id,
    brand = bc.brand,
    model = bc.model,
    variant = bc.variant,
    engineCc = bc.engineCc,
    price = bc.price,
    leaseAmount = bc.leaseAmount,
    leaseTenure = bc.leaseTenure,
    mileage = bc.mileage,
    images = bc.images,
    availabilityStatus = bc.availabilityStatus,
    insuranceDetails = bc.insuranceDetails
  )

  fun searchBikeCatalog(
    request: BikeCatalogSearchRequest,
    pageable: Pageable
  ): Page<BikeCatalogResponse> {

    val search = BikeCatalogSpecification.searchBikeCatalog(request)

    return bikeCatalogRepository.findAll(search, pageable ).map {
      toResponse(it)
    }
  }

  fun updateBikeCatalog(request: BikeCatalogRequest): BikeCatalogResponse {
    bikeCatalog(request.id ?: 0)

    val bikeCatalog = toBikeCatalogEntity(request)

    val updatedBikeCatalog = bikeCatalogRepository.save(bikeCatalog)

    return toResponse(updatedBikeCatalog)
  }

  fun deleteBikeCatalog(bikeCatalogId: Long) {
    bikeCatalog(bikeCatalogId)

    bikeCatalogRepository.deleteById(bikeCatalogId)
  }
}