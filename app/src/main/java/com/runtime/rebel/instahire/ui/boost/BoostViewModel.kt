package com.runtime.rebel.instahire.ui.boost

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runtime.rebel.instahire.model.Result
import com.runtime.rebel.instahire.model.FileData
import com.runtime.rebel.instahire.repository.home.HomeRepository
import com.runtime.rebel.instahire.utils.FirebaseHelper
import com.runtime.rebel.instahire.utils.PdfUtils
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class BoostViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _boostStatus = MutableLiveData<Result>()
    val boostStatus: LiveData<Result> get() = _boostStatus

    private val _uploadedFiles = MutableLiveData<List<FileData>>()
    val uploadedFiles: LiveData<List<FileData>> get() = _uploadedFiles

    private val _uploadingUserFileStatus = MutableLiveData<Result>()
    val uploadingStatus: LiveData<Result> get() = _uploadingUserFileStatus

    private val _uploadedUserFileUrl = MutableLiveData<String?>()
    val uploadedFileUrl: LiveData<String?> get() = _uploadedUserFileUrl

    private val _uploadingGeneratedFileStatus = MutableLiveData<Result>()
    val uploadingGeneratedFileStatus: LiveData<Result> get() = _uploadingGeneratedFileStatus

    private val _generatedText = MutableLiveData<String>()
    val generatedText: LiveData<String> get() = _generatedText

    init {
        getUploadedFiles()
    }

    fun getUploadedFiles() {
        viewModelScope.launch {
            _uploadedFiles.value = homeRepository.getUploadedFiles()
        }
    }


    fun processResumeEnhancement(
        context: Context,
        pdfUrl: String,
        jobUrl: String,
        jobDescription: String
    ) {

        _boostStatus.value = Result.Loading
        try {
            FirebaseHelper.downloadPDF(context, pdfUrl) { pdfPath ->
                if (pdfPath != null) {
                    val pdfExtractedText = PdfUtils.extractTextFromPDF(pdfPath)
                    viewModelScope.launch {
                        val enhancedText =
                            homeRepository.processPdf(
                                pdfExtractedText,
                                pdfUrl,
                                jobUrl,
                                jobDescription
                            )
                        enhancedText?.let {

                            _generatedText.value = it
                            _boostStatus.value = Result.Success
                            return@launch

                        }
                        Timber.d("Enhanced text: $enhancedText")

                        _boostStatus.value =
                            Result.Error("Failed creating PDF. Enhanced text $enhancedText")
                    }
                } else {
                    _boostStatus.value = Result.Error("PDF download failed")
                }
            }
        } catch (e: Exception) {
            _boostStatus.value = Result.Error(e.message ?: "Unknown error")
        }
    }

    fun deleteFile(file: FileData) {
        viewModelScope.launch {
            homeRepository.deleteFile(file)
            _uploadedFiles.value = homeRepository.getUploadedFiles()
        }
    }

    fun uploadUserFile(selectedPdfUri: Uri, fileNameFromUri: String?) {
        viewModelScope.launch {
            _uploadingUserFileStatus.value = Result.Loading
            try {
                val fileUrl = homeRepository.uploadFile(selectedPdfUri, fileNameFromUri)
                _uploadedUserFileUrl.value = fileUrl
                _uploadingUserFileStatus.value = Result.Success
            } catch (e: Exception) {
                _uploadingUserFileStatus.value = Result.Error(e.message ?: "Unkown error")
            }

        }

    }

    fun uploadGeneratedFile(absolutePath: String, fileName: String) {
        viewModelScope.launch {
            _uploadingGeneratedFileStatus.value = Result.Loading
            try {
                homeRepository.uploadFile(Uri.fromFile(File(absolutePath)), fileName)
                _uploadingGeneratedFileStatus.value = Result.Success
            } catch (e: Exception) {
                _uploadingGeneratedFileStatus.value = Result.Error(e.message ?: "Unkown error")
            }
        }
    }


}