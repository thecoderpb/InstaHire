package com.runtime.rebel.instahire.vm.home

import androidx.lifecycle.ViewModel
import com.runtime.rebel.instahire.repository.home.HomeRepository
import javax.inject.Inject

class HomeActivityViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    fun isUserLoggedIn() = repository.isUserLoggedIn()
    fun signOutUser() = repository.signOutUser()
}