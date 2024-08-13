package com.example.airpulse.notification
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.airpulse.MainActivity
import com.example.airpulse.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
class FirebaseService : FirebaseMessagingService() {
    val channelId = "airpulse_high_aqi_channel"
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            // Process the data here
        }
        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            val messageBody = it.body
            val messageTitle = it.title
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Show the notification
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            NotificationManagerCompat.from(this).notify(1, builder.build())
        }
    }

    fun sendNotification(title: String?, message: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = channelId

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "AirPulse Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Create an Intent to launch the MainActivity
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        // Create a PendingIntent to wrap the Intent
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)  // Set the PendingIntent to the notification
            .setAutoCancel(true)  // Automatically remove the notification when clicked

        notificationManager.notify(0, notificationBuilder.build())

        Log.d("FirebaseService", "test");
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FirebaseService", token)
        // Send token to your server if needed
    }
}