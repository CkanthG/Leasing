package com.bike.catalog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BikeCatalogApplication

fun main(args: Array<String>) {
  runApplication<BikeCatalogApplication>(*args)
}
