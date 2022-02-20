package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var forecastButton: Button

    private val apiKey = "f3a570e81b7875eb46127cbffd05a448"
    private lateinit var api: Api
    private lateinit var cityName: TextView
    private lateinit var tempIcon: ImageView
    private lateinit var currentTemp: TextView
    private lateinit var tempHi: TextView
    private lateinit var tempLo: TextView
    private lateinit var tempHumid: TextView
    private lateinit var tempPressure: TextView
    private lateinit var feelLike: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        forecastButton = findViewById(R.id.forecast_button)
        forecastButton.setOnClickListener {
            startActivity(Intent(this, ForecastActivity::class.java))
        }

        cityName = findViewById(R.id.city_name)
        tempIcon = findViewById(R.id.temp_icon)
        currentTemp = findViewById(R.id.temp)
        tempHi = findViewById(R.id.temp_high)
        tempLo = findViewById(R.id.temp_low)
        tempHumid = findViewById(R.id.temp_humid)
        tempPressure = findViewById(R.id.temp_pressure)
        feelLike = findViewById(R.id.temp_feel)

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()

        api = retrofit.create(Api::class.java)
    }

    override fun onResume() {
        super.onResume()
        val call: Call<CurrentCondition> = api.getCurrentConditions("55101")
        call.enqueue(object : Callback<CurrentCondition> {
            override fun onResponse(
                call: Call<CurrentCondition>,
                response: Response<CurrentCondition>
            ) {
                val currentConditions = response.body()
                currentConditions?.let {
                    bindData(it)
                }
            }
            override fun onFailure(call: Call<CurrentCondition>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun bindData(currentCondition: CurrentCondition) {
        cityName.text = currentCondition.name
        currentTemp.text = getString(R.string.temp, currentCondition.main.temp.toInt())
        tempHi.text = getString(R.string.temp_high, currentCondition.main.tempMax.toInt())
        tempLo.text = getString(R.string.temp_low, currentCondition.main.tempMin.toInt())
        tempHumid.text = getString(R.string.temp_humid, currentCondition.main.humidity.toInt())
        tempPressure.text = getString(R.string.temp_pressure, currentCondition.main.pressure.toInt())
        feelLike.text = getString(R.string.temp_feel, currentCondition.main.feelsLike.toInt())
        val iconName = currentCondition.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this).load(iconUrl).into(tempIcon)
    }
}