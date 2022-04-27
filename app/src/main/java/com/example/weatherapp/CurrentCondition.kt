package com.example.weatherapp

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentCondition (
    val weather: List<WeatherCondition>,
    val main: Current,
    val name: String,
    @Json(name = "coord") val coordinates: Coordinates,
 ) : Parcelable