package com.djangoroid.android.hackathon.network

import com.djangoroid.android.hackathon.network.dto.LoginRequest
import com.djangoroid.android.hackathon.network.dto.LoginResult
import com.djangoroid.android.hackathon.network.dto.SignupRequest
import com.djangoroid.android.hackathon.network.dto.SignupResult
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import com.djangoroid.android.hackathon.network.dto.MyNotes
import com.djangoroid.android.hackathon.network.dto.NoteSummary
import com.djangoroid.android.hackathon.network.dto.OpenNotes


interface RestService {
    @POST("user/login/")
    suspend fun login(@Body request: LoginRequest): LoginResult

    @POST("user/logout/")
    suspend fun logout()

    @POST("user/register/")
    suspend fun signup(@Body request: SignupRequest): SignupResult

    @GET("api/myNote")
    suspend fun myNoteList(): MyNotes

    @GET("api/openNote")
    suspend fun openNoteList(): OpenNotes
}