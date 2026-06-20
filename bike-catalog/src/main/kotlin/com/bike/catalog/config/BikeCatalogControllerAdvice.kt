package com.bike.catalog.config

import com.bike.catalog.dto.response.BikeCatalogResponse
import com.bike.catalog.exception.BikeCatalogException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class BikeCatalogControllerAdvice {

  @ExceptionHandler(BikeCatalogException::class)
  fun handleBikeCatalogException(ex: BikeCatalogException): ProblemDetail {
    return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message)
  }

  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun methodArgumentNotValidException(methodArgumentNotValidException: MethodArgumentNotValidException): ProblemDetail {
    val errorMap = HashMap<String, String>()
    methodArgumentNotValidException.fieldErrors.forEach {
      it.defaultMessage?.let { value -> errorMap[it.field] = value }
    }

    val errorMsgKey = if(errorMap.size <= 1) "error" else "errors";
    val problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)
    problemDetail.setProperty(errorMsgKey, errorMap)

    return problemDetail
  }

  @ExceptionHandler(HttpMessageNotReadableException::class)
  fun httpMessageNotReadableException(exception: HttpMessageNotReadableException): ProblemDetail {
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.message)
  }

}