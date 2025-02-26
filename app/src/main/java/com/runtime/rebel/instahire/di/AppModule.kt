package com.runtime.rebel.instahire.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.runtime.rebel.instahire.api.FindWorkAPI
import com.runtime.rebel.instahire.repository.home.HomeRepository
import com.runtime.rebel.instahire.repository.login.LoginRepository
import com.runtime.rebel.instahire.vm.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    fun provideHomeRepository(firebaseAuth: FirebaseAuth, jobApi: FindWorkAPI): HomeRepository {
        return HomeRepository(firebaseAuth, jobApi)
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