package com.runtime.rebel.instahire.repository.login

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

/**
 * Repository to handle the login process.
 * Use repository for login, registration and password reset screens.
 */
class LoginRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    fun signInUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null) // Success
                } else {
                    onResult(false, task.exception?.message) // Error message
                }
            }
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun signUpUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null) // Success
                } else {
                    onResult(false, task.exception?.message) // Error message
                }
            }
    }
}