package com.example.weatherapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Adapter(private val data: List<DayForecast>) : RecyclerView.Adapter<Adapter.ViewHolder>(){

    @SuppressLint("NewApi")
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val formatDate = DateTimeFormatter.ofPattern("MMM dd")
        private val formatTime = DateTimeFormatter.ofPattern("h:mm a")
        private val dateView: TextView = view.findViewById(R.id.date)
        private val sunriseView: TextView = view.findViewById(R.id.time_sunrise)
        private val sunsetView: TextView = view.findViewById(R.id.time_sunset)
        private val tempView: TextView = view.findViewById(R.id.temp_day)
        private val tempHiView: TextView = view.findViewById(R.id.temp_Hi)
        private val tempLoView: TextView = view.findViewById(R.id.temp_Lo)
        private val imgView: ImageView = view.findViewById(R.id.temp_img)

        fun bind(data: DayForecast){
            val instant = Instant.ofEpochSecond(data.date)
            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            val instant2 = Instant.ofEpochSecond(data.sunrise)
            val dateTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault())
            val instant3 = Instant.ofEpochSecond(data.sunset)
            val dateTime3 = LocalDateTime.ofInstant(instant3, ZoneId.systemDefault())

            sunriseView.append(formatTime.format(dateTime2))
            sunsetView.append(formatTime.format(dateTime3))
            dateView.text = formatDate.format(dateTime)
            tempView.text = itemView.context.getString(R.string.temp_day, data.temp.day.toInt())
            tempHiView.text = itemView.context.getString(R.string.temp_max, data.temp.max.toInt())
            tempLoView.text = itemView.context.getString(R.string.temp_min, data.temp.min.toInt())

            val iconName = data.weather.firstOrNull()?.icon
            val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
            Glide.with(itemView).load(iconUrl).into(imgView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Adapter.ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

}