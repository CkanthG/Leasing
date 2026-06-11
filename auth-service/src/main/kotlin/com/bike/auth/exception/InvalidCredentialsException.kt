package com.bike.auth.exception

class InvalidCredentialsException(override val message: String?): RuntimeException(message) {
}