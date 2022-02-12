package com.example.weatherapp

class DayForecast (
    var date: Long,
    var sunrise: Long,
    var sunset: Long,
    var temp: ForecastTemp,
    var pressure: Float,
    var humidity: Int
    )