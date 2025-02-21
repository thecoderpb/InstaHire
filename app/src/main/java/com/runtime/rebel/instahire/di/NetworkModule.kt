package com.runtime.rebel.instahire.di

import com.runtime.rebel.instahire.api.IndeedAPI
import com.runtime.rebel.instahire.api.OpenAiAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideIndeedRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.indeed.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideOpenAiRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideIndeedApiService(retrofit: Retrofit): IndeedAPI =
        retrofit.create(IndeedAPI::class.java)

    @Provides
    @Singleton
    fun provideOpenAiApiService(retrofit: Retrofit): OpenAiAPI =
        retrofit.create(OpenAiAPI::class.java)
}