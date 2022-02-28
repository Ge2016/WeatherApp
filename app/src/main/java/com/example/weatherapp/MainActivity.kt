package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forecastButton.setOnClickListener {
            startActivity(Intent(this, ForecastActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentCondition.observe(this) { currentCondition ->
            bindData(currentCondition)
        }
        viewModel.loadData()
    }

    private fun bindData(currentCondition: CurrentCondition) {
        binding.cityName.text = currentCondition.name
        binding.temp.text = getString(R.string.temp, currentCondition.main.temp.toInt())
        binding.tempHigh.text = getString(R.string.temp_high, currentCondition.main.tempMax.toInt())
        binding.tempLow.text = getString(R.string.temp_low, currentCondition.main.tempMin.toInt())
        binding.tempHumid.text = getString(R.string.temp_humid, currentCondition.main.humidity.toInt())
        binding.tempPressure.text = getString(R.string.temp_pressure, currentCondition.main.pressure.toInt())
        binding.tempFeel.text = getString(R.string.temp_feel, currentCondition.main.feelsLike.toInt())
        val iconName = currentCondition.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this).load(iconUrl).into(binding.tempIcon)
    }
}