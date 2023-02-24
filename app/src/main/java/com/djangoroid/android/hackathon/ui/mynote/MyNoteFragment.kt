package com.djangoroid.android.hackathon.ui. mynote

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
import com.djangoroid.android.hackathon.R
import com.djangoroid.android.hackathon.databinding.FragmentMynoteBinding
import com.djangoroid.android.hackathon.util.AuthStorage
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyNoteFragment: Fragment() {

    private lateinit var binding: FragmentMynoteBinding
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private val viewModel: MyNoteViewModel by viewModel()
    private lateinit var adapter: MyNoteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = MyNoteListAdapter{nav()}

        lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    if (!it.isLoading) {
                        swipeRefresh?.isRefreshing = false

                        if (it.isError) {
                            Toast.makeText(context, it.ErrorMessage,Toast.LENGTH_SHORT).show()
                        } else {
                            adapter.submitList(it.myNoteSummary)
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
        binding = FragmentMynoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("check", "check")

        /*
        lifecycleScope.launch {
            authStorage.authInfo.collect {
                if (it == null) {
                    Log.d("MyNoteFragment", "start navigate to login_graph")
                    findNavController().navigate(R.id.action_global_login_graph)
                }
            }
        }
         */
        binding.myNoteRecyclerView.layoutManager = GridLayoutManager(this.context,1)
        binding.myNoteRecyclerView.adapter = adapter

        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0){
                    binding.myNoteRecyclerView.scrollToPosition(0)
                }
            }
        })


        swipeRefresh = binding.swipeRefreshLayout
        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    fun nav(){
        this.findNavController().navigate(MyNoteFragmentDirections.actionMyNoteFragmentToNoteFragment())
    }
}