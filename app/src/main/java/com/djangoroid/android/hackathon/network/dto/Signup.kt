package com.djangoroid.android.hackathon.network.dto

data class SignupRequest(
    val username: String,
    val password: String,
    val nickname: String,
    val tags: List<String>
)

data class SignupResult(
    val detail: String
)