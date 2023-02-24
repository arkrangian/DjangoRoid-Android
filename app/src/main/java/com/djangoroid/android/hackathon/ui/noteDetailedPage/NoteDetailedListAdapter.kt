package com.djangoroid.android.hackathon.ui.noteDetailedPage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djangoroid.android.hackathon.databinding.FileItemBinding
import com.djangoroid.android.hackathon.databinding.NoteItemBinding
import com.djangoroid.android.hackathon.network.dto.NoteSummary
import com.djangoroid.android.hackathon.ui.mynote.MyNoteListAdapter

class NoteDetailedListAdapter(

): ListAdapter<String, NoteDetailedListAdapter.NoteDetailedViewHolder>(DiffCallback) {

    class NoteDetailedViewHolder(
        private val binding: FileItemBinding, private val context: Context
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(tempData: String) {
            Glide.with(context)
                .asBitmap()
                .load(tempData)
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteDetailedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NoteDetailedListAdapter.NoteDetailedViewHolder(
            FileItemBinding.inflate(layoutInflater, parent, false), parent.context
        )
    }

    override fun onBindViewHolder(holder: NoteDetailedViewHolder, position: Int) {
        val getData = getItem(position)
        holder.bind(getData)
    }

    companion object DiffCallback: DiffUtil.ItemCallback<String>() {
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}