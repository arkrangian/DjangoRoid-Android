package com.djangoroid.android.hackathon.ui.mynote

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MyNoteUiState(
    val title: String = "",
    val desc: String = "",
    val image: String = "", // TODO : 이미지 형식이 String 맞는지?
    val isLoading: Boolean = false
)

class MyNoteViewModel: ViewModel() {
    private val _uiState: MutableStateFlow<MyNoteUiState> = MutableStateFlow(MyNoteUiState())
    val uiState: StateFlow<MyNoteUiState> = _uiState



}