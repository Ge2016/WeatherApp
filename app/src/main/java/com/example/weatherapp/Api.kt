package com.example.weatherapp

import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("weather")
    suspend fun getCurrentCondition(
        @Query("zip") zip: String,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "f3a570e81b7875eb46127cbffd05a448"
    ): CurrentCondition

    @GET("weather")
    suspend fun getCurrentConditions(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "f3a570e81b7875eb46127cbffd05a448"
    ): CurrentCondition

    @GET("forecast/daily")
    suspend fun getForecast(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("cnt") count: Int = 16,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "f3a570e81b7875eb46127cbffd05a448"
    ): Forecast
}
