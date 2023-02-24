package com.djangoroid.android.hackathon.network.dto

data class UserDTO(
    val id: Int,
    val username: String,
    val nickname: String,
    val tags: List<String>,
)

data class AuthStorageUserDTO(
    val id: Int,
    val username: String,
)