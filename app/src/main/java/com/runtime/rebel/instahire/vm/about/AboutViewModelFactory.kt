package com.runtime.rebel.instahire.vm.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.runtime.rebel.instahire.vm.home.HomeActivityViewModel

class AboutViewModelFactory(): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AboutActivityViewModel::class.java)) {
            return AboutActivityViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}