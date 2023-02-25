package com.djangoroid.android.hackathon.network.dto

//import com.squareup.moshi.Json
//
//data class MyNotes(
//    @Json(name="next") val next: String?,
//    @Json(name="previous") val previous: String?,
//    @Json(name="results") val myNotes: List<NoteSummary>
//)
//
//data class OpenNotes(
//    @Json(name="next") val next: String?,
//    @Json(name="previous") val previous: String?,
//    @Json(name="results") val openNotes: List<NoteSummary>
//)
//
//data class NoteSummary(
//    val id: Int,
//    val thumbnail: String?,
//    val title: String,
//    @Json(name="nickname") val adminName: String,
//    @Json(name="created_by") val adminId: Int,
//    @Json(name="description") val desc: String,
//    @Json(name="fork_count") val fork: Int,
//    @Json(name="waffle_count") val like: Int,
//    @Json(name="is_public") val isPublic: Boolean,
//)
//
//data class NoteData(
//    val id: Int,
//    val title: String,
//    @Json(name="description") val desc: String,
//    @Json(name="is_public") val isPublic: Boolean,
//    val thumbnail: String?,
//    @Json(name="nickname") val adminName: String,
//    @Json(name="created_by") val adminId: Int,
//    @Json(name="fork_count") val fork: Int,
//    @Json(name="waffle_count") val like: Int,
//    @Json(name="canvas") val canvasPk: Int?
//)
//
//data class ImagesData(
//    @Json(name="id") val canvasId: Int,
//    @Json(name="pages") val images: List<ImageInform>
//)
//
//data class ImageInform(
//    @Json(name="id") val imageId: Int,
//    val page: Int,
//    @Json(name="background") val url: String,
//    @Json(name="canvas") val canvasId: Int
//)
//
//data class CreateRequest(
//    @Json(name="title") val title:String,
//    @Json(name="description") val desc: String,
//    @Json(name="thumbnail") val thumbnail: String?,
//)