package com.djangoroid.android.hackathon.data.note.myNote

import com.djangoroid.android.hackathon.data.note.myNote.source.MyNoteDataSource
import com.djangoroid.android.hackathon.util.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class Note(
    val title: String,
    val desc: String,
    val img: String
)

data class MyNoteData(
    val myNotes: List<Note> = listOf(),
    val isError: Boolean = false,
    val errorMessage: String? = null,
)

class MyNoteRepository(
    private val myNoteDataSource: MyNoteDataSource
) {

    private val _myNoteData: MutableStateFlow<MyNoteData> = MutableStateFlow(MyNoteData())
    val myNoteData: StateFlow<MyNoteData> = _myNoteData

    suspend fun refreshMyNote() {
        val result = myNoteDataSource.refreshMyNote()
        when (result) {
            is ApiResult.Success -> {
                _myNoteData.update { it.copy(myNotes = result.data, isError = false, errorMessage = null) }
            }
            is ApiResult.Error -> {
                _myNoteData.update { it.copy(isError = true, errorMessage = result.message) }
            }
            is ApiResult.Exception -> {
                _myNoteData.update { it.copy(isError = true, errorMessage = "System Corruption") }
            }
        }
    }

}