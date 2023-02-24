package com.djangoroid.android.hackathon.ui.fileList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djangoroid.android.hackathon.data.note.noteDetail.NoteDetailRepository
import com.djangoroid.android.hackathon.network.dto.NoteData
import com.djangoroid.android.hackathon.ui.noteDetailedPage.NoteDetailedUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FileListUiState(
    val noteDetailData: NoteData?,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val ErrorMessage: String? = null
)
class FileListViewModel(
    noteDetailRepository: NoteDetailRepository
): ViewModel() {

    private val _fileListUiState: MutableStateFlow<FileListUiState> = MutableStateFlow(
        FileListUiState(null)
    )
    val fileListUiState: StateFlow<FileListUiState> = _fileListUiState

    init {
        viewModelScope.launch {
            noteDetailRepository.detailNoteData
                .collect{ data ->
                    _fileListUiState.update {
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

}