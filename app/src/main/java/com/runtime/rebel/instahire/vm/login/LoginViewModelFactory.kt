package com.runtime.rebel.instahire.vm.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.runtime.rebel.instahire.repository.login.LoginRepository
import javax.inject.Inject

class LoginViewModelFactory @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginActivityViewModel::class.java)) {
            return LoginActivityViewModel(loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}