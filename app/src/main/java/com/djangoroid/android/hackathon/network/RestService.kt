package com.djangoroid.android.hackathon.network

import com.djangoroid.android.hackathon.network.dto.*
import okhttp3.MultipartBody
import retrofit2.http.*


interface RestService {
    @POST("accounts/login/")
    suspend fun login(@Body request: LoginRequest): LoginResult

    @POST("accounts/logout/")
    suspend fun logout()

    @POST("accounts/signup/")
    suspend fun signup(@Body request: SignupRequest): SignupResult

    @GET("notes/{userPk}")
    suspend fun myNoteList(
        @Path("userPk") userPk: Int,
    ): MyNotes

    @GET("api/openNote")
    suspend fun openNoteList(): OpenNotes

    @Multipart
    @POST("notes/{userPk}/{notePk}/canvas/")
    suspend fun createNoteCanvas(
        @Path("userPk") userPk: Int,
        @Path("notePk") notePk: Int,
        @Part images: MultipartBody.Part
    ): CreateNoteCanvasResult

    @PATCH("notes/{userPk}/{notePk}/canvas/{canvasPk}/")
    suspend fun updateNoteCanvas(
        @Path("userPk") userPk: Int,
        @Path("notePk") notePk: Int,
        @Path("canvasPk") canvasPk: Int,
        @Part images: MultipartBody.Part
    ): CreateNoteCanvasResult


    @GET("api/noteDetail")
    suspend fun getNoteDetail(
        //@Path("id") noteId: Int,
    ): NoteData
}