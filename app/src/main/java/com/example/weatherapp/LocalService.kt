package com.example.weatherapp

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.util.*

class LocalService : Service() {
    private val binder = LocalBinder()
    private val mGenerator = Random()

    val randomNumber: Int get() = mGenerator.nextInt(100)

    inner class LocalBinder : Binder() {
        fun getService(): LocalService = this@LocalService
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }
}