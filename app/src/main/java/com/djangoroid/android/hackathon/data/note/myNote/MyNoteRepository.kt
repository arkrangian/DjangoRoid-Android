package com.djangoroid.android.hackathon.data.note.myNote

import android.util.Log
import com.djangoroid.android.hackathon.data.note.myNote.source.MyNoteDataSource
import com.djangoroid.android.hackathon.network.dto.NoteSummary
import com.djangoroid.android.hackathon.util.ApiResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MyNoteData(
    val myNotes: List<NoteSummary> = listOf(),
    val isError: Boolean = false,
    val errorMessage: String? = null,
)

class MyNoteRepository(
    private val myNoteDataSource: MyNoteDataSource
) {

    private val _myNoteData: MutableSharedFlow<MyNoteData> = MutableSharedFlow(replay = 1)
    val myNoteData: SharedFlow<MyNoteData> = _myNoteData

    suspend fun refreshMyNote(userId: Int) {
        when (val result = myNoteDataSource.refreshMyNoteList(userId)) {
            is ApiResult.Success -> {
                _myNoteData.emit (MyNoteData(myNotes = result.data, isError = false, errorMessage = null))
            }
            is ApiResult.Error -> {
                _myNoteData.emit (MyNoteData(myNotes = listOf(), isError = true, errorMessage = result.message))
            }
            is ApiResult.Exception -> {
                _myNoteData.emit (MyNoteData(myNotes = listOf(), isError = true, errorMessage = "System Corruption"))
            }
        }
    }

}