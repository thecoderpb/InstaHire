package com.runtime.rebel.instahire.vm.login

import androidx.lifecycle.ViewModel
import com.runtime.rebel.instahire.repository.login.LoginRepository
import javax.inject.Inject

/**
 *  ViewModel for the login screen
 */
class LoginActivityViewModel @Inject constructor(
    private val repository: LoginRepository,
) : ViewModel() {

}