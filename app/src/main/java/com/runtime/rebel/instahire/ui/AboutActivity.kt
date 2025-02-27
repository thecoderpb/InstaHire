package com.runtime.rebel.instahire.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.R
import com.runtime.rebel.instahire.vm.about.AboutActivityViewModel
import com.runtime.rebel.instahire.vm.about.AboutViewModelFactory
import com.runtime.rebel.instahire.vm.login.LoginActivityViewModel
import javax.inject.Inject

class AboutActivity : AppCompatActivity() {

    @Inject
    lateinit var aboutViewModelFactory: AboutViewModelFactory
    private lateinit var viewModel: AboutActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_about)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inject dependencies
        (application as App).appComponent.inject(this)

        // Get the ViewModel using the custom factory
        viewModel = ViewModelProvider(this, aboutViewModelFactory)[AboutActivityViewModel::class.java]

    }
}