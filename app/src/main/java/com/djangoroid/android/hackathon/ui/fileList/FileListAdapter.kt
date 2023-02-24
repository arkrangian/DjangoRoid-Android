package com.djangoroid.android.hackathon.ui.fileList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djangoroid.android.hackathon.databinding.FileItemBinding
import com.djangoroid.android.hackathon.ui.noteDetailedPage.NoteDetailedListAdapter

class FileListAdapter(
    private val navToEditor: (String) -> Unit
): ListAdapter<String, FileListAdapter.ImageViewHolder>(DiffCallback) {
    class ImageViewHolder(
        private val binding: FileItemBinding, private val context: Context, private val navToEditor: (String) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(tempData: String) {
            Glide.with(context)
                .asBitmap()
                .load(tempData)
                .into(binding.image)
            binding.image.setOnClickListener {
                navToEditor(tempData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ImageViewHolder(
            FileItemBinding.inflate(layoutInflater, parent, false), parent.context, navToEditor
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
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