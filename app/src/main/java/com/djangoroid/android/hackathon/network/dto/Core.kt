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

data class NoteDTO(
    val id: Int,
    val title: String,
    val description: String,
    val created_at: String,
    val updated_at: String,
    val is_public: Boolean,
    val history: String,
    val fork_count: Int,
    val waffle_count: Int,
    val created_by: Int,
    val tags: List<String>

)