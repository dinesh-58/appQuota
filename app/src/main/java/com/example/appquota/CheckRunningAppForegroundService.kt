package com.example.appquota

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class CheckRunningAppForegroundService: Service() {
    companion object {
        private const val NOTIFICATION_ID = 1058
//        TODO make this notification id unique using hashCode.
//         else, might conflict with other notifications with same id
        private const val CHANNEL_ID = "AppQuotaForegroundServiceChannel"
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize your service here (if needed)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        TODO check current foreground app here, before calling startForegroundService()

        startForegroundService()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startForegroundService() {
        // Create a notification for the foreground service
        val notification = createNotification()

        // Start the foreground service
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotification(): Notification {
        // Create and return the notification to be displayed
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "AppQuota Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        return ( NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("AppQuota")
            .setContentText("AppQuota is running in the background")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//                android sets a default notif icon if not specified
            .build() )
    }
}