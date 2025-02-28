package com.runtime.rebel.instahire.api

import com.runtime.rebel.instahire.model.GptPdfRequest
import com.runtime.rebel.instahire.model.GptResponse
import com.runtime.rebel.instahire.model.JobItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Retrofit API interface definition using coroutines.
 */
interface OpenAiAPI {

    @POST("v1/chat/completions")
    suspend fun processPdf(
        @Body request: GptPdfRequest
    ): GptResponse

}


