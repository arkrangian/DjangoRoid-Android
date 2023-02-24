package com.djangoroid.android.hackathon.data.note.openNote

import com.djangoroid.android.hackathon.data.note.myNote.MyNoteData
import com.djangoroid.android.hackathon.data.note.openNote.source.OpenNoteDataSource
import com.djangoroid.android.hackathon.network.dto.NoteSummary
import com.djangoroid.android.hackathon.util.ApiResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class OpenNoteData(
    val myNotes: List<NoteSummary> = listOf(),
    val isError: Boolean = false,
    val errorMessage: String? = null,
)

class OpenNoteRepository(
    private val openNoteDataSource: OpenNoteDataSource
) {

    private val _openNoteData: MutableSharedFlow<OpenNoteData> = MutableSharedFlow(replay = 1)
    val openNoteData: SharedFlow<OpenNoteData> = _openNoteData

    init {
        GlobalScope.launch {
            refreshOpenNote()
        }
    }

    suspend fun refreshOpenNote() {
        when (val result = openNoteDataSource.getOpenNoteList()) {
            is ApiResult.Success -> {
                _openNoteData.emit (OpenNoteData(myNotes = result.data, isError = false, errorMessage = null))
            }
            is ApiResult.Error -> {
                _openNoteData.emit (OpenNoteData(myNotes = listOf(), isError = true, errorMessage = result.message))
            }
            is ApiResult.Exception -> {
                _openNoteData.emit (OpenNoteData(myNotes = listOf(), isError = true, errorMessage = "System Corruption"))
            }
        }
    }
}