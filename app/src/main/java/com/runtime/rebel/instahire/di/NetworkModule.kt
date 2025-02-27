package com.runtime.rebel.instahire.di

import com.runtime.rebel.instahire.api.FindWorkAPI
import com.runtime.rebel.instahire.api.OpenAiAPI
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton
import com.runtime.rebel.instahire.BuildConfig

@Module
object NetworkModule {

    @Provides
    @Singleton
    @Named("jobApi")
    fun provideFindWorkRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://findwork.dev/api/")
        .client(provideFindWorkHttpClient(this.provideLoggingInterceptor()))
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @Named("chatApi")
    fun provideOpenAiRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideFindWorkApiService( @Named("jobApi")retrofit: Retrofit): FindWorkAPI =
        retrofit.create(FindWorkAPI::class.java)

    @Provides
    @Singleton
    fun provideOpenAiApiService( @Named("chatApi")retrofit: Retrofit): OpenAiAPI =
        retrofit.create(OpenAiAPI::class.java)

    // Provides Logging Interceptor
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    // Provides OkHttpClient with Interceptors
    @Provides
    @Singleton
    fun provideFindWorkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Token ${BuildConfig.FINDWORK_API_KEY}") // Add your API key
                    .build()
                chain.proceed(request)
            }
            .build()
    }
}