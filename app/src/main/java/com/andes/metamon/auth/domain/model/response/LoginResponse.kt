package com.andes.metamon.auth.domain.model.response

data class LoginResponse(
    val id: Long,
    val email: String,
    val token: String
)