package com.runtime.rebel.instahire.ui.settings

import androidx.lifecycle.ViewModel
import com.runtime.rebel.instahire.repository.home.HomeRepository
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel() {

    fun signOutUser() {
        homeRepository.signOutUser()

    }

}