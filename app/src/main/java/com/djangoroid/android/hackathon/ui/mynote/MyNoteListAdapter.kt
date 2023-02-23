package com.djangoroid.android.hackathon.ui.mynote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.djangoroid.android.hackathon.databinding.NoteItemBinding

data class NoteData(
    val NoteTitle: String
)

class MyNoteListAdapter: ListAdapter<NoteData, MyNoteListAdapter.NoteViewHolder>(DiffCallback) {

    class NoteViewHolder(
        private val binding: NoteItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(noteData: NoteData) {
            binding.noteTitle.text = noteData.NoteTitle
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NoteViewHolder (
            NoteItemBinding.inflate(layoutInflater, parent, false)
                )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val getData = getItem(position)
        holder.bind(getData)
    }

    companion object DiffCallback: DiffUtil.ItemCallback<NoteData>() {
        override fun areContentsTheSame(oldItem: NoteData, newItem: NoteData): Boolean {
            TODO("Not yet implemented")
        }

        override fun areItemsTheSame(oldItem: NoteData, newItem: NoteData): Boolean {
            TODO("Not yet implemented")
        }
    }
}