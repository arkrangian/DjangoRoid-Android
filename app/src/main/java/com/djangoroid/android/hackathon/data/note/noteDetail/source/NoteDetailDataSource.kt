package com.djangoroid.android.hackathon.data.note.noteDetail.source

import com.djangoroid.android.hackathon.network.RestService
import com.djangoroid.android.hackathon.network.dto.NoteData
import com.djangoroid.android.hackathon.util.ApiResult
import com.djangoroid.android.hackathon.util.handleApi

/**
 *  노트에 대한 여러가지 동작이 가능
 *  (포크, 좋아요, 파일보기, 댓글보기 등등)
 */
class NoteDetailDataSource(
    private val restService: RestService
) {
    suspend fun getNoteDetail(noteId: Int): ApiResult<NoteData> {
        return handleApi { restService.getNoteDetail() }
    }
}