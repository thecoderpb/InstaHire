package com.runtime.rebel.instahire

import android.app.Application
import com.google.firebase.FirebaseApp
import com.runtime.rebel.instahire.di.AppComponent
import com.runtime.rebel.instahire.di.DaggerAppComponent

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

    }
}