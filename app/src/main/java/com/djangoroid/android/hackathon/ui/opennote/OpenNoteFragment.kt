package com.djangoroid.android.hackathon.ui.opennote

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.djangoroid.android.hackathon.databinding.FragmentOpennoteBinding
import com.djangoroid.android.hackathon.ui.mynote.MyNoteFragmentDirections
import com.djangoroid.android.hackathon.ui.mynote.MyNoteListAdapter
import com.djangoroid.android.hackathon.ui.mynote.MyNoteViewModel
import com.djangoroid.android.hackathon.util.AuthStorage
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class OpenNoteFragment: Fragment() {
    private lateinit var binding: FragmentOpennoteBinding
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private val viewModel: OpenNoteViewModel by viewModel()
    private val authStorage: AuthStorage by inject()
    private lateinit var adapter: OpenNoteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = OpenNoteListAdapter{nav()}

        lifecycleScope.launch {
            viewModel.openNoteUiState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    if (!it.isLoading) {
                        swipeRefresh?.isRefreshing = false

                        if (it.isError) {
                            Toast.makeText(context, it.ErrorMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            adapter.submitList(it.openNoteSummary)
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
        binding= FragmentOpennoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.openNoteRecyclerView.layoutManager = GridLayoutManager(this.context,1)
        binding.openNoteRecyclerView.adapter = adapter

        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0){
                    binding.openNoteRecyclerView.scrollToPosition(0)
                }
            }
        })


        swipeRefresh = binding.swipeRefreshLayout
        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    fun nav(){
        this.findNavController().navigate(OpenNoteFragmentDirections.actionOpenNoteFragmentToNoteDetailedFragment())
    }
}