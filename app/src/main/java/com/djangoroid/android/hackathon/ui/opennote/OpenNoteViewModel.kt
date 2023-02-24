package com.djangoroid.android.hackathon.ui.opennote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djangoroid.android.hackathon.data.note.openNote.OpenNoteRepository
import com.djangoroid.android.hackathon.network.dto.NoteSummary
import com.djangoroid.android.hackathon.ui.mynote.MyNoteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OpenNoteUiState(
    val openNoteSummary: List<NoteSummary>,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val ErrorMessage: String? = null
)

class OpenNoteViewModel(
    private val openNoteRepository: OpenNoteRepository
): ViewModel() {
    private val _openNoteUiState: MutableStateFlow<OpenNoteUiState> = MutableStateFlow(OpenNoteUiState(listOf()))
    val openNoteUiState: StateFlow<OpenNoteUiState> = _openNoteUiState

    init {
        viewModelScope.launch {
            openNoteRepository.openNoteData
                .collect{ openNoteData ->
                    _openNoteUiState.update {
                        it.copy(
                            openNoteSummary = openNoteData.myNotes,
                            isLoading = false,
                            isError = openNoteData.isError,
                            ErrorMessage = openNoteData.errorMessage
                        )
                    }
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _openNoteUiState.update { it.copy(isLoading = true) }
            openNoteRepository.refreshOpenNote()
        }
    }
}