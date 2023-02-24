package com.djangoroid.android.hackathon.ui.noteDetailedPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.djangoroid.android.hackathon.databinding.FileItemBinding
import com.djangoroid.android.hackathon.databinding.NoteItemBinding
import com.djangoroid.android.hackathon.network.dto.NoteSummary
import com.djangoroid.android.hackathon.ui.mynote.MyNoteListAdapter

class NoteDetailedListAdapter(

): ListAdapter<TempData, NoteDetailedListAdapter.NoteDetailedViewHolder>(DiffCallback) {

    class NoteDetailedViewHolder(
        private val binding: FileItemBinding,
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(tempData: TempData) {
            binding.apply {
                tempText.text = tempData.testString
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteDetailedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NoteDetailedListAdapter.NoteDetailedViewHolder(
            FileItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteDetailedViewHolder, position: Int) {
        val getData = getItem(position)
        holder.bind(getData)
    }

    companion object DiffCallback: DiffUtil.ItemCallback<TempData>() {
        override fun areContentsTheSame(oldItem: TempData, newItem: TempData): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: TempData, newItem: TempData): Boolean {
            return oldItem.testString == newItem.testString
        }
    }
}