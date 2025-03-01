package com.runtime.rebel.instahire.ui.files

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runtime.rebel.instahire.model.FileData
import com.runtime.rebel.instahire.model.Result
import com.runtime.rebel.instahire.repository.home.HomeRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilesViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uploadedFiles = MutableLiveData<List<FileData>>()
    val uploadedFiles: LiveData<List<FileData>> get() = _uploadedFiles

    private val _uploadedUserFileStatus = MutableLiveData<Result>()
    val uploadedStatus: LiveData<Result> get() = _uploadedUserFileStatus

    init {
        getUploadedFiles()
    }

    fun getUploadedFiles() {
        viewModelScope.launch {
            _uploadedUserFileStatus.value = Result.Loading
            try {
                _uploadedFiles.value = homeRepository.getUploadedFiles(
                    isDeletable = true,
                )
                _uploadedUserFileStatus.value = Result.Success
            }catch (e: Exception) {
                _uploadedUserFileStatus.value = Result.Error(e.localizedMessage ?: "Unknown error")

            }

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