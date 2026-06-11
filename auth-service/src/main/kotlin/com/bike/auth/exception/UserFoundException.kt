package com.bike.auth.exception

class UserFoundException(override val message: String?): RuntimeException(message) {
}