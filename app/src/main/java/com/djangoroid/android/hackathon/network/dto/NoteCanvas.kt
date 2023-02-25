package com.djangoroid.android.hackathon.network.dto

data class CreateNoteCanvasRequest(
    val images: List<List<String>>,
)

data class CreateNoteCanvasResult(
    val id: Int,
    val created_at: String,
    val updated_at: String,
    val pages: List<List<String>>,
    val images: List<List<String>>
)

data class UpdateNoteCanvasRequest(
    val images: List<List<String>>
)

data class UpdateNoteCanvasResult(
    val id: Int,
    val created_at: String,
    val updated_at: String,
    val pages: List<PageDTO>
)