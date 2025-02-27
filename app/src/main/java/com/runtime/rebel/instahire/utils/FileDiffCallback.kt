package com.runtime.rebel.instahire.utils

import androidx.recyclerview.widget.DiffUtil
import com.runtime.rebel.instahire.model.FileData

class FileDiffCallback : DiffUtil.ItemCallback<FileData>() {
    override fun areItemsTheSame(oldItem: FileData, newItem: FileData): Boolean {
        // Check if the items are the same based on a unique identifier, like an ID
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: FileData, newItem: FileData): Boolean {
        // Check if the content of the items is the same
        return oldItem == newItem
    }
}