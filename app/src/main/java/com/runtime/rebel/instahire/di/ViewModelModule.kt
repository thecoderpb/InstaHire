package com.runtime.rebel.instahire.di

import androidx.lifecycle.ViewModel
import com.runtime.rebel.instahire.ui.boost.BoostViewModel
import com.runtime.rebel.instahire.ui.dashboard.DashboardViewModel
import com.runtime.rebel.instahire.ui.files.FilesViewModel
import com.runtime.rebel.instahire.ui.job.JobPostingViewModel
import com.runtime.rebel.instahire.ui.login.LoginViewModel
import com.runtime.rebel.instahire.ui.registration.RegistrationViewModel
import com.runtime.rebel.instahire.ui.reset.ForgotPasswordViewModel
import com.runtime.rebel.instahire.ui.settings.SettingsViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BoostViewModel::class)
    abstract fun bindAboutViewModel(viewModel: BoostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    abstract fun bindLRegistrationViewModel(viewModel: RegistrationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(viewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgotPasswordViewModel::class)
    abstract fun bindForgotPasswordViewModel(viewModel: ForgotPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobPostingViewModel::class)
    abstract fun bindJobPostingViewModel(viewModel: JobPostingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FilesViewModel::class)
    abstract fun bindFilesViewModel(viewModel: FilesViewModel): ViewModel

}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

