package com.djangoroid.android.hackathon.network

import com.djangoroid.android.hackathon.network.dto.*
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface RestService {
    @POST("accounts/login/")
    suspend fun login(@Body request: LoginRequest): LoginResult

    @POST("accounts/logout/")
    suspend fun logout(
        @Body request: LogoutRequest
    )

    @POST("accounts/signup/")
    suspend fun signup(@Body request: SignupRequest): SignupResult

    @GET("notes/{userPk}")
    suspend fun myNoteList(
        @Path("userPk") userPk: Int,
    ): MyNotes

    @GET("recommend/")
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


    @GET("notes/{userPk}/{noteId}")
    suspend fun getNoteDetail(
        @Path("userPk") userPk: Int,
        @Path("noteId") noteId: Int
    ): NoteData

    @GET("notes/{userPk}/{noteId}/canvas/{canvasId}/")
    suspend fun getImages(
        @Path("userPk") userPk: Int,
        @Path("noteId") noteId: Int,
        @Path("canvasId") canvasId: Int,
    ): ImagesData

    @Multipart
    @POST("notes/{userPk}/create/")
    suspend fun createNote(
        @Path("userPk") userPk: Int,
        @Part("title") title: String,
        @Part("description") description: String,
        @Part thumbnail: MultipartBody.Part?,
    ): NoteData

    @Multipart
    @POST("notes/{userPk}/{notePk}/canvas/")
    suspend fun postFiles(
        @Path("userPk") userPk: Int,
        @Path("notePk") notePk: Int,
        @Part images: List<MultipartBody.Part>,
    )

}