package com.runtime.rebel.instahire.ui.settings

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.R
import com.runtime.rebel.instahire.databinding.FragmentForgotPasswordBinding
import com.runtime.rebel.instahire.databinding.FragmentSettingsBinding
import com.runtime.rebel.instahire.ui.AboutActivity
import com.runtime.rebel.instahire.ui.LoginActivity
import com.runtime.rebel.instahire.ui.reset.ForgotPasswordViewModel
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDarkModeToggle()
        setupNotificationToggle()
        setupLogout()
        setupAboutUs()
    }

    private fun setupDarkModeToggle() {
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        binding.switchDarkMode.isChecked = nightMode == AppCompatDelegate.MODE_NIGHT_YES

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            requireActivity().recreate()
        }
    }

    private fun setupNotificationToggle() {
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), "Notifications Enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Notifications Disabled", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setupLogout() {
        binding.btnLogout.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Logout")
//            builder.setMessage("InstaHire helps job seekers create AI-powered resumes.\nVersion: 1.0.0")
            builder.setMessage("Are you sure you want to logout?")
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                viewModel.signOutUser()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            builder.show()


        }
    }

    private fun setupAboutUs() {
        binding.tvAboutUs.setOnClickListener {
            startActivity(Intent(requireContext(), AboutActivity::class.java))
        }
    }
}