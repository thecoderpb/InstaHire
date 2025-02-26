package com.runtime.rebel.instahire.api

import com.runtime.rebel.instahire.model.JobResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface definition using coroutines.
 *
 * Always add '@Query("Authorization") authorization: String' param to all endpoints
 */
interface FindWorkAPI {

    @GET("jobs")
    suspend fun getJobListings(
        @Query("Authorization") authorization: String,
        @Query("page") page: Int?
    ): JobResponse

}