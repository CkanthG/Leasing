package com.bike.catalog.service

import com.bike.catalog.dto.request.BikeCatalogSearchRequest
import com.bike.catalog.entity.BikeCatalog
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object BikeCatalogSpecification {

  fun searchBikeCatalog(
    request: BikeCatalogSearchRequest,
  ): Specification<BikeCatalog> {

    return Specification { root, _, cb ->

      val predicates = mutableListOf<Predicate>()

      request.bikeCatalogId?.let {
        predicates.add(
          cb.equal(root.get<Long>("id"), it)
        )
      }

      request.brand?.let {
        predicates.add(
          cb.like(cb.lower(root.get("brand")), "%${it.lowercase()}%")
        )
      }

      request.model?.let {
        predicates.add(
          cb.like(cb.lower(root.get("model")), "%${it.lowercase()}%")
        )
      }

      request.price?.let {
        predicates.add(
          cb.lessThanOrEqualTo(
            root.get("price"), it
          )
        )
      }

      request.mileage?.let {
        predicates.add(
          cb.lessThanOrEqualTo(
            root.get("mileage"), it
          )
        )
      }

      request.available?.let {
        predicates.add(
          cb.equal(
            root.get<Boolean>("availabilityStatus"), it
          )
        )
      }

      request.insurance?.let {
        predicates.add(
          cb.like(cb.lower(root.get("insuranceDetails")), "%${it.lowercase()}%")
        )
      }

      cb.and(*predicates.toTypedArray())
    }
  }
}