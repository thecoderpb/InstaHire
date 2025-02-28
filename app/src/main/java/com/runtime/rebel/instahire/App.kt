package com.runtime.rebel.instahire

import android.app.Application
import com.google.firebase.FirebaseApp
import com.runtime.rebel.instahire.di.AppComponent
import com.runtime.rebel.instahire.di.DaggerAppComponent
import timber.log.Timber

/**
 * Main App component to enable DI throughout the app.
 * DO NOT MODIFY UNNECESSARILY
 */
class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        appComponent = DaggerAppComponent.builder()
            .build()
        appComponent
            .inject(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
}