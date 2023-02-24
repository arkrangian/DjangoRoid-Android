package com.djangoroid.android.hackathon.ui.noteDetailedPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djangoroid.android.hackathon.data.note.noteDetail.NoteDetailRepository
import com.djangoroid.android.hackathon.network.dto.NoteData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoteDetailedUiState(
    val noteDetailData: NoteData?,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val ErrorMessage: String? = null
)

class NoteDetailedViewModel(
    private val noteDetailRepository: NoteDetailRepository
): ViewModel() {

    private val _noteDetailedUiState: MutableStateFlow<NoteDetailedUiState> = MutableStateFlow(
        NoteDetailedUiState(null)
    )
    val noteDetailedUiState: StateFlow<NoteDetailedUiState> = _noteDetailedUiState

    init {
        viewModelScope.launch {
            noteDetailRepository.detailNoteData
                .collect{ data ->
                    _noteDetailedUiState.update {
                        it.copy(
                            noteDetailData = data.noteDetailData,
                            isLoading = false,
                            isError = data.isError,
                            ErrorMessage = data.errorMessage,
                        )
                    }
                }
        }
    }

    fun getData(noteId: Int) {
        viewModelScope.launch {
            _noteDetailedUiState.update { it.copy(isLoading = true) }
            noteDetailRepository.getDetailNoteData(noteId)
        }
    }



    /*


    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            myNoteRepository.refreshMyNote()
        }
    }



     */

}