package com.djangoroid.android.hackathon.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.djangoroid.android.hackathon.network.dto.AuthStorageUserDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthStorage(
    context: Context
) {
    // Shared preference stores data(key-value) on the app folder. When user removes the app the data is also removed.
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(SharedPreferenceName, Context.MODE_PRIVATE)
    private val _authInfo: MutableStateFlow<AuthInfo?> =
        MutableStateFlow(
            if ((sharedPref.getString(AccessTokenKey, "") ?: "").isEmpty()) {
                null
            } else {
                AuthInfo(
                    accessToken = sharedPref.getString(AccessTokenKey, "")!!,
                    AuthStorageUserDTO(
                        id = sharedPref.getInt(UserIdKey, -1),
                        username = sharedPref.getString(EmailKey, "")!!,
                    )
                )
            }
        )

    val authInfo: StateFlow<AuthInfo?> = _authInfo

    fun setAuthInfo(accessToken: String, user: AuthStorageUserDTO) {
        Log.d("AuthStorage", "set auth info")
        _authInfo.value = AuthInfo(accessToken, user)
        sharedPref.edit {
            putString(AccessTokenKey, accessToken)
            putInt(UserIdKey, user.id)
            putString(EmailKey, user.username)
        }
    }

    data class AuthInfo(
        val accessToken: String,
        val user: AuthStorageUserDTO,
    )

    companion object {
        const val AccessTokenKey = "access_token"
        const val EmailKey = "email"
        const val UserIdKey = "user_id"
        const val SharedPreferenceName = "auth_pref"
    }
}