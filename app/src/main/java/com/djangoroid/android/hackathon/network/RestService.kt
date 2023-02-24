package com.djangoroid.android.hackathon.network

import com.djangoroid.android.hackathon.network.dto.MyNotes
import com.djangoroid.android.hackathon.network.dto.NoteSummary
import com.djangoroid.android.hackathon.network.dto.OpenNotes
import retrofit2.http.GET

interface RestService {
    @GET("api/myNote")
    suspend fun myNoteList(): MyNotes

    @GET("api/openNote")
    suspend fun openNoteList(): OpenNotes
}