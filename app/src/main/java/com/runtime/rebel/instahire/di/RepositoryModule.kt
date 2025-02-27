package com.runtime.rebel.instahire.di

import com.google.firebase.auth.FirebaseAuth
import com.runtime.rebel.instahire.repository.home.HomeRepository
import com.runtime.rebel.instahire.repository.login.LoginRepository
import com.runtime.rebel.instahire.vm.about.AboutViewModelFactory
import com.runtime.rebel.instahire.vm.home.HomeViewModelFactory
import com.runtime.rebel.instahire.vm.login.LoginViewModelFactory
import com.runtime.rebel.instahire.vm.pdf.PdfViewerViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginViewModelFactory(loginRepository: LoginRepository): LoginViewModelFactory {
        return LoginViewModelFactory(loginRepository)
    }

    @Provides
    @Singleton
    fun provideHomeViewModelFactory(homeRepository: HomeRepository): HomeViewModelFactory {
        return HomeViewModelFactory(homeRepository)
    }

    @Provides
    @Singleton
    fun provideAboutRepository(): AboutViewModelFactory {
        return AboutViewModelFactory()
    }

    @Provides
    @Singleton
    fun providePdfViewViewModelFactory(): PdfViewerViewModelFactory {
        return  PdfViewerViewModelFactory()
    }
}