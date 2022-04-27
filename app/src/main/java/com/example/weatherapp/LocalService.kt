package com.example.weatherapp

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.view.ContentInfoCompat
import java.util.*

class LocalService : Service() {
    private val binder = LocalBinder()
    private val mGenerator = Random()

    val randomNumber: Int get() = mGenerator.nextInt(100)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE)}

        val notification: Notification = Notification.Builder(this, "S1")
            .setSmallIcon(R.drawable.bell).setContentTitle("Weather Notification")
            .setContentText("Check updated weather condition.")
            .setContentIntent(pendingIntent).build()

        startForeground(1, notification)
        return START_REDELIVER_INTENT
    }

    inner class LocalBinder : Binder() {
        fun getService(): LocalService = this@LocalService
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }
}