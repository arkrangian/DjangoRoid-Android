package com.djangoroid.android.hackathon.ui.mynote.newNote

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djangoroid.android.hackathon.databinding.FileItemBinding

class CreateNewNoteListAdapter(

) : ListAdapter<ImageStorage, CreateNewNoteListAdapter.NewPostImageViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewPostImageViewHolder {
        return NewPostImageViewHolder(
            FileItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: NewPostImageViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class NewPostImageViewHolder(
        private var binding: FileItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            image: ImageStorage,
        ) {
            Glide.with(context)
                .asBitmap()
                .load(image.byteArray)
                .into(binding.image)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ImageStorage>() {
            override fun areContentsTheSame(
                oldItem: ImageStorage,
                newItem: ImageStorage
            ): Boolean {
                return oldItem.imageRequest == newItem.imageRequest
            }

            override fun areItemsTheSame(
                oldItem: ImageStorage,
                newItem: ImageStorage
            ): Boolean {
                return oldItem.imageRequest.imageId == newItem.imageRequest.imageId
            }
        }
    }
}
