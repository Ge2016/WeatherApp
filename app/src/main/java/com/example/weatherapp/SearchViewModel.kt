package com.example.weatherapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val service: Api) : ViewModel() {
    private var zipCode: String? = null
    private var lat: Double = 0.0
    private var lon: Double = 0.0

    private val _enableButton = MutableLiveData(false)
    private val _showErrorDialog = MutableLiveData(false)
    private val _location = MutableLiveData<CurrentCondition>()

    val showErrorDialog: LiveData<Boolean>
        get() = _showErrorDialog
    val enableButton: LiveData<Boolean>
        get() = _enableButton
    val currentCondition: LiveData<CurrentCondition>
        get() = _location

    fun updateZipCode(zipCode: String) {
        if (zipCode != this.zipCode) {
            this.zipCode = zipCode
            _enableButton.value = isValidZipCode(zipCode)
        }
    }

    private fun isValidZipCode(zipCode: String): Boolean {
        return zipCode.length == 5 && zipCode.all { it.isDigit() }
    }

    fun loadLatLon() = runBlocking {
        launch {
            _location.value = service.getCurrentConditions(lat.toString(), lon.toString())
        }
    }

    fun loadZip() = runBlocking {
        launch {
            _location.value = zipCode?.let { service.getCurrentCondition(it) }
        }
    }

    fun updateLatLong(latitude: Double, longitude: Double) {
        if (latitude != this.lat && longitude != this.lon) {
            this.lat = latitude
            this.lon = longitude
        }
    }
}