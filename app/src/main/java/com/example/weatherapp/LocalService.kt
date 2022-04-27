package com.example.weatherapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class LocalService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val binder = LocalBinder()
    private val mGenerator = Random()
    val randomNumber: Int get() = mGenerator.nextInt(100)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        createNotificationChannel()

        val pendingIntent: PendingIntent =
            Intent(this, Api::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE)}

        val notification: Notification = Notification.Builder(this, "1")
            .setSmallIcon(R.drawable.bell).setContentTitle("Weather Notification")
            .setContentText("Check updated weather condition.")
            .setContentIntent(pendingIntent).build()

        startForeground(1, notification)
        return START_STICKY
    }

    inner class LocalBinder : Binder() {
        fun getService(): LocalService = this@LocalService
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "1", "Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = 1
        private const val CURRENT_CONDITION_NOTIFICATION = 5
    }
}