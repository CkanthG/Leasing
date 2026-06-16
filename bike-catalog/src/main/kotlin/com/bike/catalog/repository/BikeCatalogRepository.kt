package com.bike.catalog.repository

import com.bike.catalog.entity.BikeCatalog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface BikeCatalogRepository: JpaRepository<BikeCatalog, Long>, JpaSpecificationExecutor<BikeCatalog> {
}