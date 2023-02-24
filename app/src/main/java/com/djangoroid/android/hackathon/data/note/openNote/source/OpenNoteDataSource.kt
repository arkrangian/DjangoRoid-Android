package com.djangoroid.android.hackathon.data.note.openNote.source

import com.djangoroid.android.hackathon.network.RestService
import com.djangoroid.android.hackathon.network.dto.NoteSummary
import com.djangoroid.android.hackathon.util.ApiResult
import com.djangoroid.android.hackathon.util.handleApi

class OpenNoteDataSource(
    private val restService: RestService
) {
    suspend fun getOpenNoteList(): ApiResult<List<NoteSummary>> {
        return handleApi { restService.openNoteList().openNotes }
    }
}