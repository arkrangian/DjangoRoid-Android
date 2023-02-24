package com.djangoroid.android.hackathon.ui.mynote

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.djangoroid.android.hackathon.data.note.myNote.MyNoteRepository
import com.djangoroid.android.hackathon.network.dto.NoteSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MyNoteUiState(
    val myNoteSummary: List<NoteSummary>,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val ErrorMessage: String? = null
)

class MyNoteViewModel(
    private val myNoteRepository: MyNoteRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<MyNoteUiState> = MutableStateFlow(MyNoteUiState(listOf()))
    val uiState: StateFlow<MyNoteUiState> = _uiState

    init {
        viewModelScope.launch {
            myNoteRepository.myNoteData
                .collect{ myNoteData ->
                    _uiState.update {
                        it.copy(
                            myNoteSummary = myNoteData.myNotes,
                            isLoading = false,
                            isError = myNoteData.isError,
                            ErrorMessage = myNoteData.errorMessage
                        )
                    }
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            myNoteRepository.refreshMyNote()
        }
    }
}