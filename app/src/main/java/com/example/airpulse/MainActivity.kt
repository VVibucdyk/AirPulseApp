package com.example.airpulse

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.airpulse.ui.screen.AirPulseMainScreen
import com.example.airpulse.ui.theme.AirPulseTheme
import com.example.airpulse.viewmodel.AirQualityViewModel
import com.example.airpulse.worker.AirQualityWorker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FIREBASE PERMISSION", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result
            })
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private val airQualityViewModel: AirQualityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
        setContent {
            AirPulseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AirPulseMainScreen(viewModel = airQualityViewModel)
                }
            }
        }
        // Menjadwalkan pengecekan AQI secara berkala
        scheduleAqiCheck(this)
        WorkManager.getInstance(this)
            .enqueue(OneTimeWorkRequestBuilder<AirQualityWorker>().build())
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if the notification permission is already granted
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is granted, proceed with your app logic
                // Example: initialize FCM, set up notifications, etc.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                showPermissionRationale()
            } else {
                // Request the permission directly
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    private fun scheduleAqiCheck(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<AirQualityWorker>(1, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    private fun showPermissionRationale() {
        // Show a rationale dialog or other UI element explaining why the permission is needed
        AlertDialog.Builder(this)
            .setTitle("Notification Permission Required")
            .setMessage("This app needs notification permission to send you important updates about air quality.")
            .setPositiveButton("OK") { _, _ ->
                // Request the permission after showing rationale
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}