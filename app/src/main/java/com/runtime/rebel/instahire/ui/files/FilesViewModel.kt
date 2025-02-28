package com.runtime.rebel.instahire.ui.files

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runtime.rebel.instahire.model.FileData
import com.runtime.rebel.instahire.repository.home.HomeRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilesViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uploadedFiles = MutableLiveData<List<FileData>>()
    val uploadedFiles: LiveData<List<FileData>> get() = _uploadedFiles

    init {
        getUploadedFiles()
    }

    fun getUploadedFiles() {
        viewModelScope.launch {
            _uploadedFiles.value = homeRepository.getUploadedFiles(
                isDeletable = true,
            )
        }
    }

    fun deleteFile(file: FileData) {
        viewModelScope.launch {
            homeRepository.deleteFile(file)
            _uploadedFiles.value = homeRepository.getUploadedFiles(
                isDeletable = true,
            )
        }
    }


}