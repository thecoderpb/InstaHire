package com.runtime.rebel.instahire.ui.boost

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.runtime.rebel.instahire.R
import com.runtime.rebel.instahire.databinding.ItemUploadedFileBinding
import com.runtime.rebel.instahire.model.FileData
import com.runtime.rebel.instahire.utils.FileDiffCallback

// UploadedFilesAdapter.kt
class UploadedFilesAdapter(
    private val onItemClick: (FileData) -> Unit,
    private val onDeleteClick: (FileData) -> Unit
) : ListAdapter<FileData, UploadedFilesAdapter.FileViewHolder>(FileDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding =
            ItemUploadedFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FileViewHolder(private val binding: ItemUploadedFileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(file: FileData) {
            binding.tvFileName.text = file.name
            binding.root.setOnClickListener { onItemClick(file) }
            binding.imgDelete.setOnClickListener { onDeleteClick(file) }
            binding.tvUploadDate.text = "Uploaded on: ${file.lastModified}"

            binding.imgDelete.visibility = if (file.isDeletable) View.VISIBLE else View.GONE
            if(file.isDeletable){
                binding.imgFileIcon.setImageResource(
                    if (file.isUserUploaded)
                        R.drawable.ic_cloud
                    else
                        R.drawable.ic_book
                )
                binding.tvFileName.maxLines = 2
            } else {
                binding.tvFileName.maxLines = 1
                binding.imgFileIcon.setImageResource(
                    if (file.isUserUploaded)
                        R.drawable.ic_outward
                    else
                        R.drawable.ic_inward
                )
            }




        }
    }
}