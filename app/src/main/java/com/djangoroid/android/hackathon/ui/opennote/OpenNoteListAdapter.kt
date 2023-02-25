package com.djangoroid.android.hackathon.ui.opennote

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djangoroid.android.hackathon.databinding.NoteItemBinding
import com.djangoroid.android.hackathon.network.dto.NoteSummary

class OpenNoteListAdapter(
    private val move: (NoteSummary) -> Unit
): ListAdapter<NoteSummary, OpenNoteListAdapter.NoteViewHolder>(DiffCallback) {

    class NoteViewHolder(
        private val binding: NoteItemBinding,
        private val move: (NoteSummary) -> Unit,
        private val context: Context,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(noteSummary: NoteSummary) {
            binding.apply {
                noteTitle.text = noteSummary.title
                adminName.text = noteSummary.adminName
                forksNum.text = "${noteSummary.fork} forks"
                likesNum.text = "${noteSummary.like} likes"
                noteDesc.text = noteSummary.desc
                if (noteSummary.thumbnail != null) {
                    Glide.with(context)
                        .asBitmap()
                        .load(noteSummary.thumbnail)
                        .into(binding.thumbnail)
                }
                root.setOnClickListener { move(noteSummary) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NoteViewHolder (
            NoteItemBinding.inflate(layoutInflater, parent, false), move, parent.context
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val getData = getItem(position)
        holder.bind(getData)
    }

    companion object DiffCallback: DiffUtil.ItemCallback<NoteSummary>() {
        override fun areContentsTheSame(oldItem: NoteSummary, newItem: NoteSummary): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: NoteSummary, newItem: NoteSummary): Boolean {
            return oldItem.title == newItem.title
        }
    }
}