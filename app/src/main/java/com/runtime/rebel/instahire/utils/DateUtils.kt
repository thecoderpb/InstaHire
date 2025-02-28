package com.runtime.rebel.instahire.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun convertMillisToDate(updatedTimeMillis: Long): String {
        val dateFormat =
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) // Desired format: "25 Feb 2025"
        val date = Date(updatedTimeMillis) // Convert milliseconds to Date
        return dateFormat.format(date) // Return the formatted string
    }

}