package com.djangoroid.android.hackathon.ui.mynote.newNote

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.djangoroid.android.hackathon.databinding.FragmentCreateNewnoteBinding
import com.djangoroid.android.hackathon.ui.noteDetailedPage.NoteDetailedListAdapter
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateNewNoteFragment: Fragment() {
    private val viewModel: CreateNewNoteViewModel by viewModel()
    private lateinit var binding: FragmentCreateNewnoteBinding
    private lateinit var uploadThumbnail: ActivityResultLauncher<Intent>
    private lateinit var uploadImageFiles: ActivityResultLauncher<Intent>
    private lateinit var adapter: CreateNewNoteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uploadThumbnail = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res -> saveThumbNail(res) }
        uploadImageFiles = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res -> saveFiles(res) }

        adapter = CreateNewNoteListAdapter()

        val context = this.requireContext()
        lifecycleScope.launch {
            viewModel.createNewNoteUiState
                .flowWithLifecycle(lifecycle,Lifecycle.State.STARTED)
                .collect {
                    if(it.thumbnail != null) {
                        Glide.with(context)
                            .asBitmap()
                            .load(it.thumbnail?.byteArray)
                            .into(binding.thumbnail)
                    }

                    if(it.files != null) {
                        adapter.submitList(it.files)
                    }
                }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateNewnoteBinding.inflate(inflater, container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            adminName.text = "임시닉네임. 개발하면서 바꾸자"
            files.adapter = adapter
            thumbnail.setOnClickListener { intentThumbNail() }
            viewFilesBtn.setOnClickListener { intentFiles() }
        }
    }


    /**
     * ViewModel 에 제출하기
     */
    fun submitToServer() {
        val inputTitle = binding.title.text.toString()
        val inputDesc = binding.description.text.toString()
        if (inputTitle != "" && inputDesc != "") {
            viewModel.submitToServer(
                inputTitle, inputDesc
            )
        }

    }

    /**
     * 썸네일 추가
     */
    // Intent 불러오는 코드
    fun intentThumbNail() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png", "image/jpg"))
        uploadThumbnail.launch(intent)
    }

    // ViewModel 에 넘기는 코드
    fun saveThumbNail(result: ActivityResult) {
        if(result.resultCode == Activity.RESULT_OK){
            try {
                result.data?.data?.let { returnUri ->
                    val filename = requireContext().contentResolver.query(
                        returnUri,
                        null,
                        null,
                        null,
                        null
                    )!!.use { cursor ->
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        cursor.moveToFirst()
                        cursor.getString(nameIndex)
                    }
                    Log.v("NewPostFragment", "Get Image filename")
                    val byteArray =
                        requireContext().contentResolver.openInputStream(returnUri)!!
                            .use { stream ->
                                val bytestream = ByteArrayOutputStream()
                                val buffer = ByteArray(1000)
                                var size: Int
                                while(true){
                                    size = stream.read(buffer)
                                    if(size == -1)  break
                                    bytestream.write(buffer, 0, size)
                                }
                                bytestream.toByteArray()
                            }
                    Log.v("NewPostFragment", "Get Image as ByteArray")
                    viewModel.addThumbnail(filename, byteArray)
                }
            } catch (e: java.lang.Exception) {
                Log.d("NewPostFragment", e.toString())
            }
        }
    }

    /**
     * 파일추가
     */
    // Intent 불러오는 코드
    fun intentFiles() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png", "image/jpg"))
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        uploadImageFiles.launch(intent)
    }

    // ViewModel 에 넘기는 코드
    fun saveFiles(result: ActivityResult) {
        if(result.resultCode == Activity.RESULT_OK){
            try {
                result.data?.data?.let { returnUri ->
                    val filename = requireContext().contentResolver.query(
                        returnUri,
                        null,
                        null,
                        null,
                        null
                    )!!.use { cursor ->
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        cursor.moveToFirst()
                        cursor.getString(nameIndex)
                    }
                    Log.v("NewPostFragment", "Get Image filename")
                    val byteArray =
                        requireContext().contentResolver.openInputStream(returnUri)!!
                            .use { stream ->
                                val bytestream = ByteArrayOutputStream()
                                val buffer = ByteArray(1000)
                                var size: Int
                                while(true){
                                    size = stream.read(buffer)
                                    if(size == -1)  break
                                    bytestream.write(buffer, 0, size)
                                }
                                bytestream.toByteArray()
                            }
                    Log.v("NewPostFragment", "Get Image as ByteArray")
                    viewModel.addFiles(filename, byteArray)
                }
            } catch (e: java.lang.Exception) {
                Log.d("NewPostFragment", e.toString())
            }
        }
    }
}