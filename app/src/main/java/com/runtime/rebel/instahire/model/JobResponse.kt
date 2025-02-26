package com.runtime.rebel.instahire.model



data class JobResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<JobItem>
)
