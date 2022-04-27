package com.example.weatherapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weatherapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var mService: LocalService
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as LocalService.LocalBinder
            mService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mBound = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        createNotificationChannel()
        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onStart() {
        super.onStart()
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.FOREGROUND_SERVICE) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.FOREGROUND_SERVICE) !=
            PackageManager.PERMISSION_GRANTED) {
            requestBackgroundPermissionRationale()
        } else {
            createNotification()
            Intent(this, LocalService::class.java).also { intent ->
                bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun requestNotificationPermission(){
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.FOREGROUND_SERVICE) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.FOREGROUND_SERVICE) !=
            PackageManager.PERMISSION_GRANTED) {
            requestBackgroundPermissionRationale()
        } else {
            print("No permission granted.")
            return
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun requestBackgroundPermissionRationale(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.FOREGROUND_SERVICE)) {
            AlertDialog.Builder(this).setTitle("Request")
                .setTitle("Allow this app run in the background?")
                .setNeutralButton("Ok") { _, _ ->
                    permissionRequest.launch(
                        arrayOf(Manifest.permission.FOREGROUND_SERVICE))
                }.show()
        } else {
            permissionRequest.launch(arrayOf(Manifest.permission.FOREGROUND_SERVICE))
        }
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("C1", "Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun createNotification(){
        val intent = Intent(this, LocalService::class.java).apply{
            var flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0,
            intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, "C2")
            .setSmallIcon(R.drawable.bell).setContentTitle("Weather Notification")
            .setContentText("Check updated weather condition.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true)
            .setContentIntent(pendingIntent)

        NotificationManagerCompat.from(this).notify(0, builder.build())
    }
}