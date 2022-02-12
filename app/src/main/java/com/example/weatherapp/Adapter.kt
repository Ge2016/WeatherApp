package com.example.weatherapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class Adapter(private val data: List<DayForecast>) : RecyclerView.Adapter<Adapter.ViewHolder>(){

    @SuppressLint("NewApi")
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val formatter = DateTimeFormatter.ofPattern("MMM dd")
        private val dateView: TextView = view.findViewById(R.id.date)
        private val sunriseView: TextView = view.findViewById(R.id.time_sunrise)
        private val sunsetView: TextView = view.findViewById(R.id.time_sunset)
        private val tempView: TextView = view.findViewById(R.id.temp_day)
        private val tempHiView: TextView = view.findViewById(R.id.temp_Hi)
        private val tempLoView: TextView = view.findViewById(R.id.temp_Lo)

        fun bind(data: DayForecast){
            val instant = Instant.ofEpochSecond(data.date)
            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            dateView.text = formatter.format(dateTime)
            sunriseView.append(data.sunrise.toString() + ":00am")
            sunsetView.append(data.sunset.toString() + ":00pm")

            val numTemp = data.temp.day
            val roundTemp = numTemp.roundToInt().toString()
            tempView.append(roundTemp + "°")

            val numTempHi = data.temp.max
            val roundTempHi = numTempHi.roundToInt().toString()
            tempHiView.append(roundTempHi + "°")

            val numTempLo = data.temp.min
            val roundTempLo = numTempLo.roundToInt().toString()
            tempLoView.append(roundTempLo + "°")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.date_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Adapter.ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size


}