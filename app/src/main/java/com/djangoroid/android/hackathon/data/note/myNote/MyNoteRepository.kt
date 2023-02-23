package com.djangoroid.android.hackathon.data.note.myNote

import com.djangoroid.android.hackathon.data.note.myNote.source.MyNoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Note(
    val title: String,
    val desc: String,
    val img: String
)

data class MyNote(
    val myNotes: List<Note> = listOf()
)

class MyNoteRepository(
    private val myNoteDataSource: MyNoteDataSource
) {

    private val _myNoteData: MutableStateFlow<MyNote> = MutableStateFlow(MyNote())
    val myNoteData: StateFlow<MyNote> = _myNoteData

    suspend fun refreshMyNote()

}