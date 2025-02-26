package com.runtime.rebel.instahire.repository.home

import com.google.firebase.auth.FirebaseAuth
import com.runtime.rebel.instahire.BuildConfig
import com.runtime.rebel.instahire.api.FindWorkAPI
import com.runtime.rebel.instahire.model.JobResponse
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class HomeRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @Named("jobApi") private val jobApi: FindWorkAPI
) {

    fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null

    fun signOutUser() = firebaseAuth.signOut()

    // To store the current page, starting from page 1
    private var currentPage = 1


    suspend fun getJobListings(): JobResponse {
        return try {
            // Fetch the job listings for the current page
            val jobResponse = jobApi.getJobListings(BuildConfig.FINDWORK_API_KEY, currentPage)
            Timber.d(jobResponse.toString())

//             If there is a next page, increment the page number
            if (jobResponse.next != null) {
                currentPage++
            }

            jobResponse
        } catch (e: Exception) {
            // Handle errors (e.g., network errors, API errors, etc.)
            throw e
        }
    }

//    suspend fun getJobListings(): JobResponse = jobApi.getJobListings(CREDENTIALS, currentPage)


    // Reset pagination to the first page
    fun resetPagination() {
        currentPage = 1
    }

}