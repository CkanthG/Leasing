package com.bike.catalog.controller

import com.bike.catalog.config.SecurityConfig
import com.bike.catalog.dto.request.BikeCatalogRequest
import com.bike.catalog.dto.response.BikeCatalogResponse
import com.bike.catalog.repository.BikeCatalogRepository
import com.bike.catalog.service.BikeCatalogService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig::class)
@ActiveProfiles("test")
class BikeCatalogControllerTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @Autowired
  lateinit var objectMapper: ObjectMapper

  @Autowired
  lateinit var bikeCatalogService: BikeCatalogService

  @Autowired
  lateinit var bikeCatalogRepository: BikeCatalogRepository

  @AfterEach
  fun cleanup() {
    bikeCatalogRepository.deleteAll()
  }

  @Test
  @WithMockUser(roles = ["ADMIN"])
  fun `should create bike catalog and get bike catalog and update bike catalog and search bike catalog and delete a bike catalog`() {

    val request = BikeCatalogRequest(
      brand = "BMW",
      model = "G310R",
      variant = "Sport",
      engineCc = "313",
      price = 350000.00,
      leaseAmount = 7999.99,
      leaseTenure = 48,
      mileage = 30.0,
      images = null,
      availabilityStatus = true,
      insuranceDetails = "Insurance Included"
    )

    val res = mockMvc.perform(
      multipart("/api/v1/bikeCatalog")
        .param("brand", request.brand)
        .param("model", request.model)
        .param("variant", request.variant)
        .param("engineCc", request.engineCc.toString())
        .param("price", request.price.toString())
        .param("leaseAmount", request.leaseAmount.toString())
        .param("leaseTenure", request.leaseTenure.toString())
        .param("mileage", request.mileage.toString())
        .param("availabilityStatus", request.availabilityStatus.toString())
        .param("insuranceDetails", request.insuranceDetails)
    )
      .andExpect(status().isCreated)
      .andReturn()

    val objectMapper = jacksonObjectMapper()
    val resp: BikeCatalogResponse =
      objectMapper.readValue(res.response.contentAsString, BikeCatalogResponse::class.java)

    customAsserting(resp, request)

    val res1 = mockMvc.perform(
      get("/api/v1/bikeCatalog/" + resp.id)
    ).andExpect(status().isOk)
    .andReturn()

    val resp1 = objectMapper.readValue(res1.response.contentAsString, BikeCatalogResponse::class.java)

    assertThat(resp1).isNotNull
    customAsserting(resp1, request)

    val updateBikeCatalog = request.copy(
      brand = "BMW-1", model = "G310R-1", variant = "Sport-1"
    )

    val res2 = mockMvc.perform(
      put("/api/v1/bikeCatalog/" + resp.id)
        .content(objectMapper.writeValueAsString(updateBikeCatalog))
      .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk)
    .andReturn()

    val resp2 = objectMapper.readValue(res2.response.contentAsString, BikeCatalogResponse::class.java)

    assertThat(resp2).isNotNull
    customAsserting(resp2, updateBikeCatalog)

    val res3 = mockMvc.perform(
      get("/api/v1/bikeCatalog?brand=BMW-1&model=G310R-1")
    ).andExpect(status().isOk)
    .andReturn()

    val node = objectMapper.readTree(res3.response.contentAsString)

    val content: List<BikeCatalogResponse> = objectMapper.readValue(
      node.get("content").toString(),
      object : TypeReference<List<BikeCatalogResponse>>() {}
    )

    assertThat(content).isNotNull
    customAsserting(content.first(), updateBikeCatalog)

    mockMvc.perform(
      delete("/api/v1/bikeCatalog/" + resp.id)
    ).andExpect(status().isNoContent)
  }

  private fun customAsserting(
    resp: BikeCatalogResponse,
    request: BikeCatalogRequest
  ) {
    assertThat(resp).extracting(
      BikeCatalogResponse::brand,
      BikeCatalogResponse::model,
      BikeCatalogResponse::variant,
      BikeCatalogResponse::engineCc,
      BikeCatalogResponse::price,
      BikeCatalogResponse::leaseAmount,
      BikeCatalogResponse::leaseTenure,
      BikeCatalogResponse::mileage,
      BikeCatalogResponse::availabilityStatus,
      BikeCatalogResponse::insuranceDetails
    ).containsExactly(
      request.brand, request.model, request.variant, request.engineCc, request.price, request.leaseAmount,
      request.leaseTenure, request.mileage, request.availabilityStatus, request.insuranceDetails
    )
  }

  @Test
  fun `unauthorized get bike catalog should throw forbidden`() {
    mockMvc.perform(
      get("/api/v1/bikeCatalog/999999")
    ).andExpect(status().isForbidden)
  }

  @Test
  fun `unauthorized search bike catalog should throw forbidden`() {
    mockMvc.perform(
      get("/api/v1/bikeCatalog?brand=BMW-1")
    ).andExpect(status().isForbidden)
  }

  @Test
  @WithMockUser(roles = ["USER"])
  fun `get bike catalog should throw not found`() {
    mockMvc.perform(
      get("/api/v1/bikeCatalog/999999")
    ).andExpect(status().isNotFound)
  }

  @Test
  @WithMockUser(roles = ["USER"])
  fun `search bike catalog return empty list`() {
    val res4 = mockMvc.perform(
      get("/api/v1/bikeCatalog?brand=BMW-2")
    ).andExpect(status().isOk)
      .andReturn()

    val node = objectMapper.readTree(res4.response.contentAsString)

    val content: List<BikeCatalogResponse> = objectMapper.readValue(
      node.get("content").toString(),
      object : TypeReference<List<BikeCatalogResponse>>() {}
    )

    assertThat(content).isEmpty()
  }

  @Test
  @WithMockUser(roles = ["ADMIN"])
  fun `save bike catalog return throw validation error`() {

    mockMvc.perform(
      multipart("/api/v1/bikeCatalog")
        .param("brand", "BMW")
    )
      .andExpect(status().isBadRequest)
  }
}