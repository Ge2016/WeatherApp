package com.example.weatherapp

data class CurrentCondition (
    val weather: List<WeatherCondition>,
    val main: Current,
    val name: String,
)