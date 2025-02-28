package com.runtime.rebel.instahire.utils

import android.content.Context
import android.os.Environment
import com.google.firebase.storage.FirebaseStorage
import java.io.File

object FirebaseHelper {
    fun downloadPDF(context: Context, fileUrl: String, callback: (String?) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(fileUrl)
        val localFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "resume.pdf")

        storageRef.getFile(localFile)
            .addOnSuccessListener { callback(localFile.absolutePath) }
            .addOnFailureListener { callback(null) }
    }
}