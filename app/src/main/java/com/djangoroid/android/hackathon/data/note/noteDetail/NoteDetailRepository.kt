package com.djangoroid.android.hackathon.data.note.noteDetail

import com.djangoroid.android.hackathon.data.note.noteDetail.source.NoteDetailDataSource
import com.djangoroid.android.hackathon.network.dto.ImagesData
import com.djangoroid.android.hackathon.network.dto.NoteData
import com.djangoroid.android.hackathon.util.ApiResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * 만약 DetailFragment 에서 벗어나게 되면 SharedFlow 를 초기화시켜야한다. ㅇㅋ?
 */
data class NoteDetailData(
    val noteDetailData: NoteData?,
    val images: ImagesData?,
    val isError: Boolean = false,
    val errorMessage: String? = null,
)

class NoteDetailRepository(
    private val noteDetailDataSource: NoteDetailDataSource
) {

    private val _detailNoteData: MutableSharedFlow<NoteDetailData> = MutableSharedFlow(replay = 1)
    val detailNoteData: SharedFlow<NoteDetailData> = _detailNoteData

    suspend fun getDetailNoteData(userId:Int, noteId: Int) {
        when (val result = noteDetailDataSource.getNoteDetail(userId, noteId)) {
            is ApiResult.Success -> {
                // 이미지를 한번 더 로드한다.
                if (result.data.canvasPk != null) {
                    when (val imageResult = noteDetailDataSource.getImages(userId, noteId, result.data.canvasPk!!)) {
                        is ApiResult.Success -> {
                            _detailNoteData.emit (NoteDetailData(noteDetailData = result.data, images = imageResult.data,isError = false, errorMessage = null))
                        }
                        else -> {
                            _detailNoteData.emit (NoteDetailData(noteDetailData = null, images = null, isError = true, errorMessage = "System Corruption"))
                        }
                    }
                } else {
                    _detailNoteData.emit (NoteDetailData(noteDetailData = result.data, images = null,isError = false, errorMessage = null))
                }
            }
            is ApiResult.Error -> {
                _detailNoteData.emit (NoteDetailData(noteDetailData = null, images = null, isError = true, errorMessage = result.message))
            }
            is ApiResult.Exception -> {
                _detailNoteData.emit (NoteDetailData(noteDetailData = null, images = null, isError = true, errorMessage = "System Corruption"))
            }
        }
    }

}