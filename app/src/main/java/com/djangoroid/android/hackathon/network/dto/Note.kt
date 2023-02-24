package com.djangoroid.android.hackathon.network.dto

data class MyNotes(
    val myNotes: List<NoteSummary>
)

data class OpenNotes(
    val openNotes: List<NoteSummary>
)

data class NoteSummary(
    val thumbnail: String,
    val title: String,
    val adminName: String,
    val desc: String,
    val fork: Int,
    val like: Int,
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