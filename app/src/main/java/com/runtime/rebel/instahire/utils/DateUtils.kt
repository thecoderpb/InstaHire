package com.runtime.rebel.instahire.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


object DateUtils {

    fun convertMillisToDate(updatedTimeMillis: Long): String {
        val dateFormat =
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) // Desired format: "25 Feb 2025"
        val date = Date(updatedTimeMillis) // Convert milliseconds to Date
        return dateFormat.format(date) // Return the formatted string
    }

    @SuppressLint("NewApi")
    fun formatDate(inputDate: String?): String {
        // Parse the input date string
        val zonedDateTime = ZonedDateTime.parse(inputDate)

        // Define the output format
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

        // Format and return the result
        return zonedDateTime.format(outputFormatter)
    }

}