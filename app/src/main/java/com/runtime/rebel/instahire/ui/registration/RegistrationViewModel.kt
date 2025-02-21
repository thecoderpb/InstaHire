package com.runtime.rebel.instahire.ui.registration

import androidx.lifecycle.ViewModel
import com.runtime.rebel.instahire.repository.login.LoginRepository
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val repository: LoginRepository
): ViewModel() {

    fun signUpUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {

        repository.signUpUser(email, password, onResult)
    }

}