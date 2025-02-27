package com.runtime.rebel.instahire.ui.boost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.runtime.rebel.instahire.databinding.ItemUploadedFileBinding
import com.runtime.rebel.instahire.model.FileData
import com.runtime.rebel.instahire.utils.FileDiffCallback

// UploadedFilesAdapter.kt
class UploadedFilesAdapter(
    private val onDeleteClick: (FileData) -> Unit
) : ListAdapter<FileData, UploadedFilesAdapter.FileViewHolder>(FileDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding = ItemUploadedFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FileViewHolder(private val binding: ItemUploadedFileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(file: FileData) {
            binding.tvFileName.text = file.name
            binding.imgDelete.setOnClickListener { onDeleteClick(file) }
        }
    }
}