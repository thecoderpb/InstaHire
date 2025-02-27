package com.runtime.rebel.instahire.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.runtime.rebel.instahire.api.FindWorkAPI
import com.runtime.rebel.instahire.api.OpenAiAPI
import com.runtime.rebel.instahire.repository.home.HomeRepository
import com.runtime.rebel.instahire.repository.login.LoginRepository
import com.runtime.rebel.instahire.utils.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    fun provideHomeRepository(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: FirebaseDatabase,
        firebaseStorage: FirebaseStorage,
        jobApi: FindWorkAPI,
        promptApi: OpenAiAPI
    ): HomeRepository {
        return HomeRepository(firebaseAuth, firebaseDatabase, firebaseStorage , jobApi, promptApi)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(firebaseAuth: FirebaseAuth): LoginRepository {
        return LoginRepository(firebaseAuth)
    }

    @Singleton
    @Provides
    fun provideViewModelFactory(
        creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return ViewModelFactory(creators)
    }
}