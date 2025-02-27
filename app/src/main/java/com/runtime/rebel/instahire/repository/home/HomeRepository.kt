package com.runtime.rebel.instahire.repository.home

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.runtime.rebel.instahire.BuildConfig
import com.runtime.rebel.instahire.api.FindWorkAPI
import com.runtime.rebel.instahire.api.OpenAiAPI
import com.runtime.rebel.instahire.model.FileData
import com.runtime.rebel.instahire.model.JobResponse
import com.runtime.rebel.instahire.model.Resume
import com.runtime.rebel.instahire.utils.Result
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

class HomeRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage,
    @Named("jobApi") private val jobApi: FindWorkAPI,
    @Named("chatApi") private val promptApi: OpenAiAPI,
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


    suspend fun uploadFile(uri: Uri, fileName: String?): String? {
        val fname = fileName ?: (UUID.randomUUID().toString() + ".pdf")
        val storageRef = firebaseStorage.reference.child("users/${firebaseAuth.currentUser?.uid}/${fname}")
        return try {
            val uploadTask = storageRef.putFile(uri).await()
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun callOpenAI(resumeUrl: String, jobUrl: String) {
        val requestBody = mapOf("resumeUrl" to resumeUrl, "jobUrl" to jobUrl)
//        promptApi.boostResume(requestBody)
    }

    suspend fun getUploadedFiles(): List<FileData> {
        // Fetch uploaded files from Firestore or Realtime DB
        val userId = firebaseAuth.currentUser?.uid ?: return emptyList()
        val userFilesRef = firebaseStorage.getReference("users").child(userId)
        val filesSnapshot = userFilesRef.listAll().await()
        val files = mutableListOf<FileData>()
        for (file in filesSnapshot.items) {
            val url = file.downloadUrl.await().toString()
            val fileName = file.name
            val fileData = FileData(fileName, url)
            files.add(fileData)
        }
        return files
    }

    suspend fun deleteFile(file: FileData) {
        firebaseStorage.getReferenceFromUrl(file.url).delete().await()
    }

}