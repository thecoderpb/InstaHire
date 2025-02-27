package com.runtime.rebel.instahire.ui.boost

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runtime.rebel.instahire.model.Result
import com.runtime.rebel.instahire.model.FileData
import com.runtime.rebel.instahire.repository.home.HomeRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class BoostViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _boostStatus = MutableLiveData<Result>()
    val boostStatus: LiveData<Result> get() = _boostStatus

    private val _uploadedFiles = MutableLiveData<List<FileData>>()
    val uploadedFiles: LiveData<List<FileData>> get() = _uploadedFiles

    private val _uploadingStatus = MutableLiveData<Result>()
    val uploadingStatus: LiveData<Result> get() = _uploadingStatus

    init {
        getUploadedFiles()
    }

    fun getUploadedFiles() {
        viewModelScope.launch {
            _uploadedFiles.value = homeRepository.getUploadedFiles()
        }
    }


    fun boostProfile(pdfUri: Uri, pdfName: String, jobUrl: String) {
        viewModelScope.launch {
            _boostStatus.value = Result.Loading
            try {
                homeRepository.uploadFile(pdfUri,pdfName)?.let { downloadUrl ->
                    homeRepository.callOpenAI(downloadUrl, jobUrl)
                    _boostStatus.value = Result.Success
                } ?: run {
                    _boostStatus.value = Result.Error("File upload failed")
                }
            } catch (e: Exception) {
                _boostStatus.value = Result.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteFile(file: FileData) {
        viewModelScope.launch {
            homeRepository.deleteFile(file)
            _uploadedFiles.value = homeRepository.getUploadedFiles()
        }
    }

    fun uploadFile(selectedPdfUri: Uri, fileNameFromUri: String?) {
        viewModelScope.launch {
            _uploadingStatus.value = Result.Loading
            try {
                homeRepository.uploadFile(selectedPdfUri,fileNameFromUri)
                _uploadingStatus.value = Result.Success
            } catch (e: Exception) {
                _uploadingStatus.value = Result.Error(e.message ?: "Unkown error")
            }

        }

    }


}