package com.runtime.rebel.instahire.model

data class JobItem (
    val id: String,
    val role: String,
    val text: String? = null,
    val companyName: String,
    val location: String,
    val url: String,
    val keywords: List<String>,
    val datePosted: String,
    val source: String
)