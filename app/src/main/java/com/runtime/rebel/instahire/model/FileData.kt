package com.runtime.rebel.instahire.model

data class FileData(
    val name: String,
    val url: String,
    val isUserUploaded: Boolean = true,
    val lastModified: String,
    val isDeletable: Boolean = false,
)