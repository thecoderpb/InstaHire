package com.runtime.rebel.instahire.api

import com.runtime.rebel.instahire.model.Account
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Retrofit API interface definition using coroutines.
 */
interface OpenAiAPI {

    @GET("login")
    suspend fun login(@Header("Authorization") credentials: String?): Account

}


