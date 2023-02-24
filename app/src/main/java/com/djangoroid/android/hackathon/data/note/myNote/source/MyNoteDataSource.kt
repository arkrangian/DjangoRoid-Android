package com.djangoroid.android.hackathon.data.note.myNote.source

import com.djangoroid.android.hackathon.network.RestService
import com.djangoroid.android.hackathon.network.dto.NoteSummary
import com.djangoroid.android.hackathon.util.ApiResult
import com.djangoroid.android.hackathon.util.handleApi

class MyNoteDataSource(
    private val restService: RestService
) {
    suspend fun refreshMyNoteList(): ApiResult<List<NoteSummary>> {
        return handleApi{ restService.myNoteList().myNotes }
    }
}