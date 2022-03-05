package com.example.weatherapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.WeatherListBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Adapter(private val data: List<DayForecast>) : RecyclerView.Adapter<Adapter.ViewHolder>(){

    @SuppressLint("NewApi")
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val formatDate = DateTimeFormatter.ofPattern("MMM dd")
        private val formatTime = DateTimeFormatter.ofPattern("h:mm a")
        private val binding: WeatherListBinding = WeatherListBinding.bind(view)

        fun bind(data: DayForecast){
            val dateInstant = Instant.ofEpochSecond(data.date)
            val dateTime = LocalDateTime.ofInstant(dateInstant, ZoneId.systemDefault())
            val sunriseInstant = Instant.ofEpochSecond(data.sunrise)
            val sunriseTime = LocalDateTime.ofInstant(sunriseInstant, ZoneId.systemDefault())
            val sunsetInstant = Instant.ofEpochSecond(data.sunset)
            val sunsetTime = LocalDateTime.ofInstant(sunsetInstant, ZoneId.systemDefault())

            binding.timeSunrise.append(formatTime.format(sunriseTime))
            binding.timeSunset.append(formatTime.format(sunsetTime))
            binding.date.text = formatDate.format(dateTime)
            binding.tempDay.text = itemView.context.getString(R.string.temp_day, data.temp.day.toInt())
            binding.tempHi.text = itemView.context.getString(R.string.temp_max, data.temp.max.toInt())
            binding.tempLo.text = itemView.context.getString(R.string.temp_min, data.temp.min.toInt())

            val iconName = data.weather.firstOrNull()?.icon
            val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
            Glide.with(itemView).load(iconUrl).into(binding.tempImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

}