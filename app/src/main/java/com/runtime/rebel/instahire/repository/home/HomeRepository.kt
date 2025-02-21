package com.runtime.rebel.instahire.repository.home

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun signOutUser(): Unit {
        firebaseAuth.signOut()
    }
}