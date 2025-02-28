package com.runtime.rebel.instahire.repository.home

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.runtime.rebel.instahire.BuildConfig
import com.runtime.rebel.instahire.api.FindWorkAPI
import com.runtime.rebel.instahire.api.OpenAiAPI
import com.runtime.rebel.instahire.model.FileData
import com.runtime.rebel.instahire.model.GptPdfRequest
import com.runtime.rebel.instahire.model.JobResponse
import com.runtime.rebel.instahire.model.Message
import com.runtime.rebel.instahire.utils.DateUtils
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



    // Reset pagination to the first page
    fun resetPagination() {
        currentPage = 1
    }


    suspend fun uploadFile(uri: Uri, fileName: String?): String? {
        val fname = fileName ?: (UUID.randomUUID().toString() + ".pdf")
        val storageRef = firebaseStorage.reference.child("users/${firebaseAuth.currentUser?.uid}/${fname}")
        return try {
            storageRef.putFile(uri).await()
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUploadedFiles(): List<FileData> {
        val userId = firebaseAuth.currentUser?.uid ?: return emptyList()
        val userFilesRef = firebaseStorage.getReference("users").child(userId)
        val filesSnapshot = userFilesRef.listAll().await()
        val files = mutableListOf<FileData>()
        for (file in filesSnapshot.items) {
            val url = file.downloadUrl.await().toString()
            val fileName = file.name
            // Get the file metadata to fetch the last modified date
            val metadata = file.metadata.await()
            val lastModifiedDate = DateUtils.convertMillisToDate(metadata.updatedTimeMillis ) // This will give you the last modified date


            // Creating FileData object with the name, url, and last modified date
            val fileData = FileData(
                fileName,
                url,
                !fileName.contains("Enhanced"),
                lastModified = lastModifiedDate)
            files.add(fileData)
        }
        return files
    }

    suspend fun deleteFile(file: FileData) {
        firebaseStorage.getReferenceFromUrl(file.url).delete().await()
    }

    suspend fun processPdf(pdfData: String, pdfUrl: String,  jobUrl: String?, jobDescription: String ): String? {
        val request = GptPdfRequest(
            messages = listOf(
                Message(role = "system", content = "Read the job description and the resume and enhance the resume based on the job description preserving formatting."),
                Message(role = "user", content = "Here's the file content: $pdfData", fileUrl = pdfData),
                Message(role = "user", content = "Here's the job content: $jobDescription", fileUrl = jobDescription),
                Message(role = "user", content = "Here's the job url: $jobUrl", fileUrl = jobUrl),
                Message(role = "user", content = "Here's the pdf url: $pdfUrl", fileUrl = jobUrl),
                Message(role = "system", content = "If no data is found, then rephrase the entire resume and send the data preserving formatting", fileUrl = jobUrl),

            )
        )

        return try {
            val response = promptApi.processPdf(request)
            response.choices.firstOrNull()?.message?.content
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

//    fun generatePDF(firebasePdfUrl: String, context: Context): Any {
//        // Step 1: Download PDF
//        FirebaseHelper.downloadPDF(context, firebasePdfUrl) { pdfPath ->
//            if (pdfPath != null) {
//                // Step 2: Extract Text from PDF
//                val extractedText = PdfUtils.extractTextFromPDF(pdfPath)
//
//                // Step 3: Enhance Text via OpenAI
//                promptApi.enhancePDFText(extractedText) { enhancedText ->
//                    if (enhancedText != null) {
//                        // Step 4: Convert Enhanced Text Back to PDF
//                        val outputPdfPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath + "/enhanced_resume.pdf"
//                        PDFGenerator.createPDF(outputPdfPath, enhancedText)
//                    }
//                }
//            }
//        }
//    }

}