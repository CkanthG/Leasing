package com.bike.auth.exception

class UserNotFoundException(override val message: String?): RuntimeException(message) {
}