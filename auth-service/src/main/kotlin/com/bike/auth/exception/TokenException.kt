package com.bike.auth.exception

class TokenException(override val message: String?): RuntimeException(message) {
}