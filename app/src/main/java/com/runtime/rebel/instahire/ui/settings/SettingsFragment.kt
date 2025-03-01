package com.runtime.rebel.instahire.ui.settings

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.runtime.rebel.instahire.App
import com.runtime.rebel.instahire.R
import com.runtime.rebel.instahire.databinding.FragmentForgotPasswordBinding
import com.runtime.rebel.instahire.databinding.FragmentSettingsBinding
import com.runtime.rebel.instahire.receiver.NotificationReceiver
import com.runtime.rebel.instahire.ui.AboutActivity
import com.runtime.rebel.instahire.ui.LoginActivity
import com.runtime.rebel.instahire.ui.reset.ForgotPasswordViewModel
import java.util.concurrent.TimeUnit

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

        binding.switchDarkMode.isChecked =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            requireActivity().recreate()
        }
    }

    private fun setupLogout() {
        binding.btnLogout.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            with(builder) {
                setTitle("Logout")
//            builder.setMessage("InstaHire helps job seekers create AI-powered resumes.\nVersion: 1.0.0")
                setMessage("Are you sure you want to logout?")
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    viewModel.signOutUser()
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                show()
            }


        }
    }

    private fun setupAboutUs() {
        binding.tvAboutUs.setOnClickListener {
            startActivity(Intent(requireContext(), AboutActivity::class.java))
        }
    }

    private fun setupNotificationToggle() {
        val sharedPrefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        binding.switchNotifications.isChecked =
            sharedPrefs.getBoolean("notification_scheduled", false)
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                requestNotificationPermission()
            } else {
                Toast.makeText(requireContext(), "Notifications Disabled", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                )
                != PackageManager.PERMISSION_GRANTED
            ) {

                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                showInstantNotification()
                checkAndScheduleNotification()
            }
        } else {
            showInstantNotification()
            checkAndScheduleNotification()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showInstantNotification()
                checkAndScheduleNotification()
            } else {
                binding.switchNotifications.isChecked = false // Revert toggle if denied
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun checkAndScheduleNotification() {
        val sharedPrefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isScheduled = sharedPrefs.getBoolean("notification_scheduled", false)

        if (!isScheduled) {
            scheduleNotification()
            sharedPrefs.edit().putBoolean("notification_scheduled", true).apply()
        }
    }

    private fun scheduleNotification() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }

        Toast.makeText(requireContext(), "Reminder set for 24 hours", Toast.LENGTH_SHORT).show()
    }

    private fun showInstantNotification() {
        val channelId = "job_reminder_channel"
        val notificationId = 1

        val notificationManager = requireContext().getSystemService(NotificationManager::class.java)

        // Create Notification Channel (Only needed for Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Job Reminder", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        // Build Notification
        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.ic_bolt)
            .setContentTitle("Wohoo!")
            .setContentText("Now you will get notified whenever your new jobs are available.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // Show Notification
        notificationManager.notify(notificationId, notification)
    }
}