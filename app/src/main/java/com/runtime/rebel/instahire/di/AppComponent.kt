package com.runtime.rebel.instahire.di

import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.ui.HomeActivity
import com.runtime.rebel.instahire.ui.LoginActivity
import com.runtime.rebel.instahire.ui.about.BoostProfileFragment
import com.runtime.rebel.instahire.ui.home.DashboardFragment
import com.runtime.rebel.instahire.ui.login.LoginFragment
import com.runtime.rebel.instahire.ui.registration.RegistrationFragment
import com.runtime.rebel.instahire.ui.reset.ForgotPasswordFragment
import com.runtime.rebel.instahire.ui.settings.SettingsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        FirebaseModule::class,
        ViewModelModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {
    fun inject(application: App)
    fun inject(homeActivity: HomeActivity)
    fun inject(loginActivity: LoginActivity)

    // Add more components here as needed
    fun inject(loginFragment: LoginFragment)
    fun inject(dashboardFragment: DashboardFragment)
    fun inject(boostProfileFragment: BoostProfileFragment)
    fun inject(registrationFragment: RegistrationFragment)
    fun inject(forgotPasswordFragment: ForgotPasswordFragment)
    fun inject(settingsFragment: SettingsFragment)

}