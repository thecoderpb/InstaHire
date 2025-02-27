package com.runtime.rebel.instahire.vm.pdf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.runtime.rebel.instahire.vm.home.HomeActivityViewModel

@Suppress("UNCHECKED_CAST")
class PdfViewerViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PdfViewerViewModel::class.java)) {
            return PdfViewerViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}