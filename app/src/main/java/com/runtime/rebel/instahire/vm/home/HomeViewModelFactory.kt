package com.runtime.rebel.instahire.vm.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.runtime.rebel.instahire.repository.home.HomeRepository
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeActivityViewModel::class.java)) {
            return HomeActivityViewModel(homeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}