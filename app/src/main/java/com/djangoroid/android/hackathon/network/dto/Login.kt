package com.djangoroid.android.hackathon.network.dto

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResult(
    val token: String,
    val user: UserDTO
)