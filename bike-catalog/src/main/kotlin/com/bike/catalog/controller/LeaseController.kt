package com.bike.catalog.controller

import com.bike.catalog.dto.request.LeaseRequest
import com.bike.catalog.dto.response.LeaseResponse
import com.bike.catalog.service.LeaseService
import org.apache.coyote.Response
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/lease")
class LeaseController(
  private val leaseService: LeaseService
) {

  @PreAuthorize("hasRole('USER')")
  @PostMapping
  fun applyForLease(
    @RequestBody request: LeaseRequest
  ): ResponseEntity<LeaseResponse> {
    return ResponseEntity.ok(leaseService.applyLease(request))
  }
}