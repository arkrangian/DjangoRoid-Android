package com.djangoroid.android.hackathon.network.dto

import com.squareup.moshi.Json

data class MyNotes(
    @Json(name="results") val myNotes: List<NoteSummary>
)

data class OpenNotes(
    val openNotes: List<NoteSummary>
)

data class NoteSummary(
    val id: Int,
    val thumbnail: String,
    val title: String,
    @Json(name="created_by") val adminName: String,
    @Json(name="description") val desc: String,
    @Json(name="fork_count") val fork: Int,
    @Json(name="waffle_count") val like: Int,
)

data class NoteData(
    val thumbnail: String,
    val title: String,
    val adminName: String,
    val desc: String,
    val fork: Int,
    val like: Int,
    val images: List<String>
)