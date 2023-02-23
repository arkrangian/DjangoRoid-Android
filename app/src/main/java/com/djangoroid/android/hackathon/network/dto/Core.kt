package com.djangoroid.android.hackathon.network.dto

data class UserDTO(
    val id: Int,
    val email: String,
    val nickname: String,
    val tag: String,
)

data class AuthStorageUserDTO(
    val id: Int,
    val email: String,
)