package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("weather")
    fun getCurrentConditions(
        @Query("zip") zip: String,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "f3a570e81b7875eb46127cbffd05a448"
    ) : Call<CurrentCondition>

    @GET("forecast/daily")
    fun getForecast(
        @Query("zip") zip: String,
        @Query("cnt") count: Int = 16,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "f3a570e81b7875eb46127cbffd05a448"
    ) : Call<Forecast>
}