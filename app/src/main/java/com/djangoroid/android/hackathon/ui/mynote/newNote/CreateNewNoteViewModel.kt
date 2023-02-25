package com.djangoroid.android.hackathon.ui.mynote.newNote

import android.provider.MediaStore.Audio.Media
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djangoroid.android.hackathon.data.note.myNote.MyNoteRepository
import com.djangoroid.android.hackathon.network.RestService
import com.djangoroid.android.hackathon.network.dto.CreateRequest
import com.djangoroid.android.hackathon.util.AuthStorage
import com.squareup.moshi.Json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.random.Random

data class CreateNewNoteUiState(
    val thumbnail: ImageStorage? = null,
    val files: MutableList<ImageStorage> = mutableListOf(),
    val isCreate: Boolean = false
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

class CreateNewNoteViewModel(
    private val authStorage: AuthStorage,
    private val myNoteRepository: MyNoteRepository,
    private val restService: RestService
): ViewModel() {
    private val _createNewNoteUiState: MutableStateFlow<CreateNewNoteUiState> = MutableStateFlow(CreateNewNoteUiState())
    val createNewNoteUiState: StateFlow<CreateNewNoteUiState> = _createNewNoteUiState

    fun submitToServer(userId: Int, title: String, desc: String) {
        viewModelScope.launch {
            try{
                /**
                 * 여기가 노트생성
                 */
                // 썸네일 multipart 자료형으로 바꾸기
                var part: MultipartBody.Part? = null
                if (_createNewNoteUiState.value.thumbnail != null){
                    val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), _createNewNoteUiState.value.thumbnail!!.byteArray)
                    part = MultipartBody.Part.createFormData("thumbnail", "thumbnail${Random.nextInt(10000)}.jpg", requestBody)
                }
                val createResponse = restService.createNote(userId, title,desc,part)
                /**
                 * 이쪽이 파일생성(파일이 있으면)
                 */
                val noteId = createResponse.id
                if (_createNewNoteUiState.value.files.isNotEmpty()){
                    val parts: MutableList<MultipartBody.Part> = mutableListOf()
                    var index = 1
                    _createNewNoteUiState.value.files.forEach {
                        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), it.byteArray)
                        parts.add(MultipartBody.Part.createFormData("images","note${noteId}_${index}.jpg",requestBody))
                        index++
                    }
                    restService.postFiles(userId,noteId,parts)
                }
                myNoteRepository.refreshMyNote(authStorage.authInfo.value!!.user.id)
                _createNewNoteUiState.update { it.copy(isCreate = true) }
            } catch (e:java.lang.Exception) {
                e
            }
        }
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