package com.djangoroid.android.hackathon.ui.updateNote

import android.util.Log
import androidx.lifecycle.ViewModel
import com.djangoroid.android.hackathon.network.RestService
import okhttp3.MultipartBody

class CanvasViewModel(
    private val restService: RestService
): ViewModel() {
    suspend fun createNoteCanvas(userPk: Int = 4, notePk: Int = 7, images: MultipartBody.Part) {
        try {
            restService.createNoteCanvas(userPk, notePk, images)
        } catch (e: Exception) {
            Log.d("CanvasViewModel", "Error: $e")
        }
    }

    suspend fun updateNoteCanvas(userPk: Int, notePk: Int, canvasPk: Int, images: MultipartBody.Part) {
        try {
            restService.updateNoteCanvas(userPk, notePk, canvasPk, images)
        } catch (e: Exception) {
            Log.d("CanvasViewModel", "Error: $e")
        }
    }
}