package com.example.airpulse.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.airpulse.MainActivity
import com.example.airpulse.R
import com.example.airpulse.data.api.AirQualityApi
import com.example.airpulse.data.api.RetrofitInstance
import com.example.airpulse.notification.FirebaseService
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AirQualityWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val apiService: AirQualityApi = RetrofitInstance.api
    private val apiKey = "d576c9ce1b626caf8b20c3ea5eaf31206daad48d"
    private val channelId = "airpulse_high_aqi_channel"

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val aqi = fetchAqiData()
                if (aqi in 61..99) {
                    sendNotification("PERHATIAN! AQI Sedang", "AQI saat ini $aqi. Harap memakai masker.")
                }else if(aqi > 100){
                    sendNotification("BERBAHAYA! AQI Tinggi", "AQI saat ini $aqi. Harap memakai masker!")
                }
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }

    private suspend fun fetchAqiData(): Int {
        val response = apiService.getAirQualityData("here", apiKey)
        Log.d("AirQualityWorker", "AQI Response: $response")
        return response.data.aqi // Ensure this is correct and handles potential null values
    }

    private fun sendNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "AirPulse Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }
}