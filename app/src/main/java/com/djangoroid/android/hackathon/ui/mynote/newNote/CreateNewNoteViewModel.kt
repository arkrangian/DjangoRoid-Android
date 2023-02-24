package com.djangoroid.android.hackathon.ui.mynote.newNote

import androidx.lifecycle.ViewModel
import com.squareup.moshi.Json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class CreateNewNoteUiState(
    val thumbnail: ImageStorage? = null,
    val files: MutableList<ImageStorage> = mutableListOf(),
)

data class ImageStorage(
    var imageRequest: ImageRequest,
    val byteArray: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageStorage

        if (imageRequest != other.imageRequest) return false
        if (!byteArray.contentEquals(other.byteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = imageRequest.hashCode()
        result = 31 * result + byteArray.contentHashCode()
        return result
    }
}

data class ImageRequest(
    @Json(name = "imageId") val imageId: Int,
    @Json(name = "fileName") val fileName: String,
    @Json(name = "description") val description: String?
)

class CreateNewNoteViewModel: ViewModel() {
    private val _createNewNoteUiState: MutableStateFlow<CreateNewNoteUiState> = MutableStateFlow(CreateNewNoteUiState())
    val createNewNoteUiState: StateFlow<CreateNewNoteUiState> = _createNewNoteUiState

    fun submitToServer(title: String, desc: String) {
        //서버에 전송!!!!!!!!!!!!!!!!!1
    }

    fun addThumbnail(filename: String, byteArray: ByteArray) {
        _createNewNoteUiState.update { it.copy(thumbnail = ImageStorage(ImageRequest(0,filename, ""),byteArray)) }
    }

    fun addFiles(filename: String, byteArray: ByteArray) {
        val beforeFiles: MutableList<ImageStorage> = mutableListOf<ImageStorage>()
        beforeFiles.addAll(_createNewNoteUiState.value.files)
        beforeFiles.add(ImageStorage(ImageRequest(beforeFiles.size+1,filename,""),byteArray))
        _createNewNoteUiState.update { it.copy(files = beforeFiles) }
    }
}