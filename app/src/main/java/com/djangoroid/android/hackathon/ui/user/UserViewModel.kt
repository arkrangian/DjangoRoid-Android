package com.djangoroid.android.hackathon.ui.user

import android.util.Log
import androidx.lifecycle.ViewModel
import com.djangoroid.android.hackathon.network.RestService
import com.djangoroid.android.hackathon.network.dto.AuthStorageUserDTO
import com.djangoroid.android.hackathon.network.dto.LoginRequest
import com.djangoroid.android.hackathon.network.dto.SignupRequest
import com.djangoroid.android.hackathon.util.AuthStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel(
    private val restService: RestService,
    private val authStorage: AuthStorage,
): ViewModel() {

    private val _userTags = MutableStateFlow<MutableList<String>>(mutableListOf())
    val userTags: StateFlow<List<String>> = _userTags
    suspend fun login(id: String, password: String) {
        try {
            val response = restService.login(LoginRequest(id, password))
            authStorage.setAuthInfo(
                response.token,
                AuthStorageUserDTO(response.user.id, response.user.username)
            )
        } catch (e: Exception) {
            Log.d("Error", "Error is occurred. Error: $e")
        }
    }

    suspend fun signup(id: String, password: String, nickname: String, tags: List<String>) {
        try {
            restService.signup(SignupRequest(id, password, nickname, tags))
        } catch (e: Exception) {
            Log.d("Error", "Error is occurred")
        }
    }

//    suspend fun signup(id: String, password: String, nickname: String) {
//        try {
//            restService.signup(SignupRequest(id, password, nickname))
//        } catch (e: Exception) {
//            Log.d("Error", "Error is occurred")
//        }
//    }
/*
    suspend fun logout() {
        try {
            restService.logout()
        } catch (e: Exception) {
            Log.d("Error", "Error is occurred")
        }
    }
*/
    fun addTags(tag: String) {
        _userTags.value.add(tag)
    }
}