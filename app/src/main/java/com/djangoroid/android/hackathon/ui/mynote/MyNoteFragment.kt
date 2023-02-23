package com.djangoroid.android.hackathon.ui.mynote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.djangoroid.android.hackathon.databinding.FragmentMynoteBinding

class MyNoteFragment: Fragment() {
    private lateinit var binding: FragmentMynoteBinding

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

        val adapter = MyNoteListAdapter()
        adapter.submitList(listOf(NoteData("C++"),NoteData("물리")))
        binding.myNoteRecyclerView.adapter = adapter
    }

    fun nav(){
        this.findNavController().navigate(MyNoteFragmentDirections.actionMyNoteFragmentToNoteFragment())
    }
}