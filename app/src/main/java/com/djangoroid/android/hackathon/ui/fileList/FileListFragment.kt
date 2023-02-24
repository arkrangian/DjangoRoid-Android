package com.djangoroid.android.hackathon.ui.fileList

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
import com.djangoroid.android.hackathon.databinding.FragmentFilelistBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FileListFragment: Fragment() {
    private lateinit var binding: FragmentFilelistBinding
    private lateinit var adapter: FileListAdapter
    private val viewModel: FileListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = FileListAdapter { navToEditor(it) }

        lifecycleScope.launch {
            viewModel.fileListUiState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    if (!it.isLoading) {
                        val data = it.noteDetailData
                        if (it.isError || (data==null)) {
                            Toast.makeText(context, it.ErrorMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            binding?.root!!.visibility = View.VISIBLE
                            binding?.apply {
                                adapter.submitList(data.images)
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
        binding = FragmentFilelistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.images.adapter = adapter
    }

    fun navToEditor(url: String) {
        findNavController().navigate(FileListFragmentDirections.actionFileListFragmentToImageEditorFragment(url))
    }
}