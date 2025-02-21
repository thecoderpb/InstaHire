package com.runtime.rebel.instahire.ui.login

import androidx.lifecycle.ViewModel
import com.runtime.rebel.instahire.repository.login.LoginRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
) : ViewModel() {

    fun signInUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        repository.signInUser(email, password, onResult)
    }

    fun isUserLoggedIn() = repository.isUserLoggedIn()
}