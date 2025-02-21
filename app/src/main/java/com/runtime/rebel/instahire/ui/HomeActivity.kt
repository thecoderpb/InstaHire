package com.runtime.rebel.instahire.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.R
import com.runtime.rebel.instahire.databinding.ActivityHomeBinding
import com.runtime.rebel.instahire.vm.home.HomeActivityViewModel
import com.runtime.rebel.instahire.vm.home.HomeViewModelFactory
import javax.inject.Inject

/**
 *  UI for the Home screen
 */
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (application as App).appComponent.inject(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        viewModel = ViewModelProvider(this, homeViewModelFactory)[HomeActivityViewModel::class.java]

        // Setup NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        // BottomNavigation setup
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

    }

    override fun onResume() {
        super.onResume()

        if (!viewModel.isUserLoggedIn()) {
            viewModel.signOutUser()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

        // Write code below this block


    }
}