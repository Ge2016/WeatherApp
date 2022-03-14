package com.example.weatherapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentCondition (
    val weather: List<WeatherCondition>,
    val main: Current,
    val name: String,
 ) : Parcelable