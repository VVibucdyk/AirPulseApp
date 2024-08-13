package com.example.airpulse.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.airpulse.R
import com.example.airpulse.data.api.AirQualityApi
import com.example.airpulse.data.api.RetrofitInstance
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AirQualityWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    val channelId = "airpulse_high_aqi_channel"
    private val apiService: AirQualityApi = RetrofitInstance.api
    private val apiKey = "d576c9ce1b626caf8b20c3ea5eaf31206daad48d"

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val aqi = fetchAqiData()
                if (aqi > 80) {
                    sendNotification("High AQI Alert", "The AQI is currently $aqi. Please take precautions.")
                }
                Result.success()
            } catch (e: Exception) {
                Log.e("AirQualityWorker", "Error fetching AQI data", e)
                Result.failure()
            }
        }
    }

    private suspend fun fetchAqiData(): Int {
        val response = apiService.getAirQualityData("here", apiKey)
        Log.d("AQITINGGI", response.toString())
        return response.data.aqi// Return the AQI value
    }

    private fun sendNotification(title: String, message: String) {
        val firebaseMessaging = FirebaseMessaging.getInstance()
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = channelId

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "AirPulse Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(0, notificationBuilder.build())
        Log.d("Terpanggil", "Apa asja");
    }
}