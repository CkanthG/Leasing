package com.bike.catalog.controller

import com.bike.catalog.dto.request.BikeCatalogRequest
import com.bike.catalog.dto.request.BikeCatalogSearchRequest
import com.bike.catalog.dto.response.BikeCatalogResponse
import com.bike.catalog.service.BikeCatalogService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/bikeCatalog")
class BikeCatalogController(
  private val bikeCatalogService: BikeCatalogService
) {

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
  fun bikeCatalog(@Valid @ModelAttribute bikeCatalogRequest: BikeCatalogRequest): ResponseEntity<BikeCatalogResponse> {
    val res = bikeCatalogService.saveBikeCatalog(bikeCatalogRequest)
    return ResponseEntity.status(HttpStatus.CREATED).body(res)
  }

  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @GetMapping("/{bikeCatalogId}")
  fun bikeCatalog(
    @PathVariable bikeCatalogId: Long
  ): ResponseEntity<BikeCatalogResponse> {
    return ResponseEntity.ok(bikeCatalogService.getBikeCatalogById(bikeCatalogId))
  }

  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @GetMapping
  fun searchBikeCatalog(
    request: BikeCatalogSearchRequest,
    pageable: Pageable
  ): ResponseEntity<Page<BikeCatalogResponse>> {
    return ResponseEntity.ok(bikeCatalogService.searchBikeCatalog(
      request, pageable
    ))
  }

  @PreAuthorize("hasAnyRole('ADMIN')")
  @PutMapping("/{bikeCatalogId}")
  fun updateBikeCatalog(
    @RequestBody request: BikeCatalogRequest,
    @PathVariable bikeCatalogId: Long
  ): ResponseEntity<BikeCatalogResponse> {
    request.id = bikeCatalogId
    return ResponseEntity.ok(bikeCatalogService.updateBikeCatalog(request))
  }

  @PreAuthorize("hasAnyRole('ADMIN')")
  @DeleteMapping("/{bikeCatalogId}")
  fun deleteBikeCatalog(
    @PathVariable bikeCatalogId: Long
  ): ResponseEntity<Void> {
    bikeCatalogService.deleteBikeCatalog(bikeCatalogId)
    return ResponseEntity.noContent().build()
  }
}