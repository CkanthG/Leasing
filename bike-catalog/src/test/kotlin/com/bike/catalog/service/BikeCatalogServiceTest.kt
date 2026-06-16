package com.bike.catalog.service

import com.bike.catalog.dto.request.BikeCatalogRequest
import com.bike.catalog.dto.request.BikeCatalogSearchRequest
import com.bike.catalog.dto.response.BikeCatalogResponse
import com.bike.catalog.entity.BikeCatalog
import com.bike.catalog.exception.BikeCatalogException
import com.bike.catalog.repository.BikeCatalogRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class BikeCatalogServiceTest {

  @InjectMocks
  lateinit var bikeCatalogService: BikeCatalogService
  @Mock
  lateinit var bikeCatalogRepository: BikeCatalogRepository

  @Test
  fun saveBikeCatalog() {
    val request = BikeCatalogRequest(
      id = 1,
      brand = "BMW",
      model = "G310R",
      variant = "Sport",
      engineCc = "313",
      price = 350000.00,
      leaseAmount = 7999.99,
      leaseTenure = 48,
      mileage = 30.0,
      images = "abcd".toByteArray(Charsets.UTF_8),
      availabilityStatus = false,
      insuranceDetails = "Insurance renewal due in 6 months"
    )
    val bikeCatalog = BikeCatalog(
      1,
      brand = "BMW",
      model = "G310R",
      variant = "Sport",
      engineCc = "313",
      price = 350000.00,
      leaseAmount = 7999.99,
      leaseTenure = 48,
      mileage = 30.0,
      images = "abcd".toByteArray(Charsets.UTF_8),
      availabilityStatus = false,
      insuranceDetails = "Insurance renewal due in 6 months"
    )
    `when`(bikeCatalogRepository.save(bikeCatalog)).thenReturn(bikeCatalog)

    val response = bikeCatalogService.saveBikeCatalog(request)

    assertThat(response).isNotNull

    assertThat(response).extracting(
      BikeCatalogResponse::id, BikeCatalogResponse::brand, BikeCatalogResponse::model, BikeCatalogResponse::variant,
      BikeCatalogResponse::price, BikeCatalogResponse::leaseAmount, BikeCatalogResponse::leaseTenure,
      BikeCatalogResponse::mileage, BikeCatalogResponse::availabilityStatus, BikeCatalogResponse::insuranceDetails
    ).containsExactly(
      request.id, request.brand, request.model, request.variant, request.price, request.leaseAmount,
      request.leaseTenure, request.mileage, request.availabilityStatus, request.insuranceDetails
    )
  }

  @Test
  fun getBikeCatalogById() {

    val bikeCatalog = BikeCatalog(
      1,
      brand = "BMW",
      model = "G310R",
      variant = "Sport",
      engineCc = "313",
      price = 350000.00,
      leaseAmount = 7999.99,
      leaseTenure = 48,
      mileage = 30.0,
      images = "abcd".toByteArray(Charsets.UTF_8),
      availabilityStatus = false,
      insuranceDetails = "Insurance renewal due in 6 months"
    )
    `when`(bikeCatalogRepository.findById(1)).thenReturn(Optional.of(bikeCatalog))

    val response = bikeCatalogService.getBikeCatalogById(1)
    assertThat(response).isNotNull

    assertThat(response).extracting(
      BikeCatalogResponse::id, BikeCatalogResponse::brand, BikeCatalogResponse::model, BikeCatalogResponse::variant,
      BikeCatalogResponse::price, BikeCatalogResponse::leaseAmount, BikeCatalogResponse::leaseTenure,
      BikeCatalogResponse::mileage, BikeCatalogResponse::availabilityStatus, BikeCatalogResponse::insuranceDetails
    ).containsExactly(
      bikeCatalog.id, bikeCatalog.brand, bikeCatalog.model, bikeCatalog.variant, bikeCatalog.price, bikeCatalog.leaseAmount,
      bikeCatalog.leaseTenure, bikeCatalog.mileage, bikeCatalog.availabilityStatus, bikeCatalog.insuranceDetails
    )
  }

  @Test
  fun getBikeCatalogById__throwsException() {
    assertThrows<BikeCatalogException> {
      bikeCatalogService.getBikeCatalogById(1)
    }
  }

  @Test
  fun searchBikeCatalog() {
    val request = BikeCatalogSearchRequest(brand = "BMW", model = "G310R")
    val bikeCatalog = BikeCatalog(
      1,
      brand = "BMW",
      model = "G310R",
      variant = "Sport",
      engineCc = "313",
      price = 350000.00,
      leaseAmount = 7999.99,
      leaseTenure = 48,
      mileage = 30.0,
      images = "abcd".toByteArray(Charsets.UTF_8),
      availabilityStatus = false,
      insuranceDetails = "Insurance renewal due in 6 months"
    )
    val pageable = PageRequest.of(0, 10)
    val pageResponse = PageImpl(listOf(bikeCatalog))
    `when`(
      bikeCatalogRepository.findAll(
        any<Specification<BikeCatalog>>(),
        eq(pageable)
      )
    ).thenReturn(pageResponse)

    val response = bikeCatalogService.searchBikeCatalog(request, pageable)
    assertThat(response).isNotNull

    assertThat(response).extracting(
      BikeCatalogResponse::brand, BikeCatalogResponse::model
    ).containsExactly(
      Tuple(request.brand, request.model)
    )
  }

  @Test
  fun updateBikeCatalog() {
    val updateRequest = BikeCatalogRequest(
      id = 1,
      brand = "BMW-1",
      model = "G310R-1",
      variant = "Sport",
      engineCc = "313",
      price = 350000.00,
      leaseAmount = 7999.99,
      leaseTenure = 48,
      mileage = 30.0,
      images = "abcd".toByteArray(Charsets.UTF_8),
      availabilityStatus = false,
      insuranceDetails = "Insurance renewal due in 6 months"
    )
    val bikeCatalog = BikeCatalog(
      1,
      brand = "BMW",
      model = "G310R",
      variant = "Sport",
      engineCc = "313",
      price = 350000.00,
      leaseAmount = 7999.99,
      leaseTenure = 48,
      mileage = 30.0,
      images = "abcd".toByteArray(Charsets.UTF_8),
      availabilityStatus = false,
      insuranceDetails = "Insurance renewal due in 6 months"
    )
    val updatedBikeCatalog = BikeCatalog(
      1,
      brand = "BMW-1",
      model = "G310R-1",
      variant = "Sport",
      engineCc = "313",
      price = 350000.00,
      leaseAmount = 7999.99,
      leaseTenure = 48,
      mileage = 30.0,
      images = "abcd".toByteArray(Charsets.UTF_8),
      availabilityStatus = false,
      insuranceDetails = "Insurance renewal due in 6 months"
    )
    `when`(bikeCatalogRepository.findById(1)).thenReturn(Optional.of(bikeCatalog))
    `when`(bikeCatalogRepository.save(any())).thenReturn(updatedBikeCatalog)

    val response = bikeCatalogService.updateBikeCatalog(updateRequest)

    assertThat(response).isNotNull
    assertThat(response).extracting(
      BikeCatalogResponse::brand, BikeCatalogResponse::model
    ).containsExactly(
      updateRequest.brand, updateRequest.model
    )
  }

  @Test
  fun updateBikeCatalog__throwsException() {
    assertThrows<BikeCatalogException> {
      bikeCatalogService.getBikeCatalogById(1)
    }
  }

  @Test
  fun deleteBikeCatalog() {
    val bikeCatalog = BikeCatalog(
      1,
      brand = "BMW",
      model = "G310R",
      variant = "Sport",
      engineCc = "313",
      price = 350000.00,
      leaseAmount = 7999.99,
      leaseTenure = 48,
      mileage = 30.0,
      images = "abcd".toByteArray(Charsets.UTF_8),
      availabilityStatus = false,
      insuranceDetails = "Insurance renewal due in 6 months"
    )
    `when`(bikeCatalogRepository.findById(1)).thenReturn(Optional.of(bikeCatalog))

    bikeCatalogService.deleteBikeCatalog(  1)

    verify(bikeCatalogRepository).deleteById(1)
  }

  @Test
  fun deleteBikeCatalog__throwsException() {
    assertThrows<BikeCatalogException> {
      bikeCatalogService.getBikeCatalogById(1)
    }
  }

}