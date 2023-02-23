package com.djangoroid.android.hackathon.ui.mynote

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.djangoroid.android.hackathon.R
import com.djangoroid.android.hackathon.databinding.FragmentMynoteBinding
import com.djangoroid.android.hackathon.util.AuthStorage
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MyNoteFragment: Fragment() {

    private lateinit var binding: FragmentMynoteBinding
    private val authStorage: AuthStorage by inject()

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

        lifecycleScope.launch {
            authStorage.authInfo.collect {
                if (it == null) {
                    Log.d("MyNoteFragment", "start navigate to login_graph")
                    findNavController().navigate(R.id.action_global_login_graph)
                }
            }
        }

        val adapter = MyNoteListAdapter{nav()}
        adapter.submitList(listOf(NoteData("C++"),NoteData("물리")))
        binding.myNoteRecyclerView.adapter = adapter
    }

    fun nav(){
        this.findNavController().navigate(MyNoteFragmentDirections.actionMyNoteFragmentToNoteFragment())
    }
}