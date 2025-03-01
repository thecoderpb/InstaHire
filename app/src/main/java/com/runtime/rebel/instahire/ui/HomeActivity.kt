package com.runtime.rebel.instahire.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
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

    @SuppressLint("SetJavaScriptEnabled")
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
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        // BottomNavigation setup
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_LABELED
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val currentDestination = navController.currentDestination?.id
            when (item.itemId) {
                R.id.dashboardFragment -> if (currentDestination != R.id.dashboardFragment) navController.navigate(
                    R.id.dashboardFragment
                )

                R.id.boostFragment -> if (currentDestination != R.id.boostFragment) navController.navigate(
                    R.id.boostFragment
                )

                R.id.filesFragment -> if (currentDestination != R.id.filesFragment) navController.navigate(
                    R.id.filesFragment
                )

                R.id.settingsFragment -> if (currentDestination != R.id.settingsFragment) navController.navigate(
                    R.id.settingsFragment
                )
            }
            true
        }

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

    override fun onBackPressed() {
        super.onBackPressed()
        findNavController(R.id.fragment_container).popBackStack(R.id.dashboardFragment,false)
    }
}