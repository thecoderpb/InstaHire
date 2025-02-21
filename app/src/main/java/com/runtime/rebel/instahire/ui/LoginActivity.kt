package com.runtime.rebel.instahire.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.R
import com.runtime.rebel.instahire.databinding.ActivityLoginBinding
import com.runtime.rebel.instahire.ui.login.LoginFragment
import com.runtime.rebel.instahire.vm.login.LoginActivityViewModel
import com.runtime.rebel.instahire.vm.login.LoginViewModelFactory
import timber.log.Timber
import javax.inject.Inject

/**
 *  UI for the login screen
 */
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var loginViewModelFactory: LoginViewModelFactory
    private lateinit var viewModel: LoginActivityViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Inject dependencies
        (application as App).appComponent.inject(this)

        // Get the ViewModel using the custom factory
        viewModel = ViewModelProvider(this, loginViewModelFactory)[LoginActivityViewModel::class.java]

        // Setup NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.login_fragment) as NavHostFragment
        navController = navHostFragment.navController


    }


}