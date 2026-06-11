package com.bike.auth.config

import com.bike.auth.exception.TokenException
import com.bike.auth.exception.UserFoundException
import com.bike.auth.exception.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthControllerAdvice {

  @ExceptionHandler(UserNotFoundException::class)
  fun userNotFoundException(userNotFoundException: UserNotFoundException): ProblemDetail {
    return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, userNotFoundException.message)
  }

  @ExceptionHandler(UserFoundException::class)
  fun userFoundException(userFoundException: UserFoundException): ProblemDetail {
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, userFoundException.message)
  }

  @ExceptionHandler(TokenException::class)
  fun tokenException(tokenException: TokenException): ProblemDetail {
    return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, tokenException.message)
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