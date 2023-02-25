package com.djangoroid.android.hackathon.ui.noteDetailedPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.djangoroid.android.hackathon.databinding.FragmentNotedetailedBinding
import com.djangoroid.android.hackathon.ui.fileList.imageEditor.ImageEditorFragmentArgs
import com.djangoroid.android.hackathon.util.AuthStorage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

data class TempData(
    val testString: String
)

class NoteDetailedFragment: Fragment() {
    private lateinit var binding: FragmentNotedetailedBinding
    private val viewModel: NoteDetailedViewModel by viewModel()
    private val authStorage: AuthStorage by inject()
    private lateinit var adapter: NoteDetailedListAdapter
    private val navigationArgs: NoteDetailedFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = NoteDetailedListAdapter()
        val context = this.requireContext()
        lifecycleScope.launch {
            viewModel.noteDetailedUiState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    if (!it.isLoading) {
                        val data = it.noteDetailData
                        if (it.isError || (data==null)) {
                            Toast.makeText(context, it.ErrorMessage,Toast.LENGTH_SHORT).show()
                        } else {
                            binding?.root!!.visibility = View.VISIBLE
                            binding?.apply {
                                title.text = data.title
                                adminName.text = data.adminName
                                forkBtn.text = "${data.fork} forks"
                                likeBtn.text = "${data.like} likes"
                                description.text = data.desc
                                if(data.thumbnail != null) {
                                    Glide.with(context)
                                        .asBitmap()
                                        .load(data.thumbnail)
                                        .into(binding.thumbnail)
                                }
                                if (it.images != null) adapter.submitList(it.images!!.images)
                            }
                        }
                    }
                }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotedetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.visibility = View.INVISIBLE

        binding.apply {
            files.adapter = adapter
            viewFilesBtn.setOnClickListener {
                findNavController().navigate(NoteDetailedFragmentDirections.actionNoteDetailedFragmentToFileListFragment())
            }
        }

        // ViewModel 통해서 데이터 불러오자
        viewModel.getData(navigationArgs.userId.toInt(), navigationArgs.noteId.toInt())
    }
}