package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ForecastActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    private val adapterData = listOf<DayForecast>(
        DayForecast(1643737732, 6, 9,
            ForecastTemp(72.1254F, 80.15456465F, 65.1545456F), 1023.0F, 100),
        DayForecast(1643824132, 4, 5,
            ForecastTemp(67.54468F, 78.564F, 55.01546F), 1023.0F, 100),
        DayForecast(1643910532, 4, 6,
            ForecastTemp(80.56512F, 90.18548F, 53.4501F), 1023.0F, 100),
        DayForecast(1643996932, 5, 7,
            ForecastTemp(70.155F, 82.56648F, 52.456465F), 1023.0F, 100),
        DayForecast(1644079148, 6, 5,
            ForecastTemp(65.0854F, 73.666F, 47.23356F),1023.0F, 100),
        DayForecast(1644165548, 4, 7,
            ForecastTemp(83.65578F, 92.456546F, 61.41F), 1023.0F, 100),
        DayForecast(1644251948, 6, 4,
            ForecastTemp(74.45450F, 86.460F, 55.05644F),1023.0F, 100),
        DayForecast(1644338348, 5, 6,
            ForecastTemp(72.0F, 84.0F, 63.0F), 1023.0F, 100),
        DayForecast(1644424748, 4, 5,
            ForecastTemp(61.0F, 76.0F, 48.0F),1023.0F, 100),
        DayForecast(1644511148, 4, 6,
            ForecastTemp(65.0F, 78.0F, 50.0F), 1023.0F, 100),
        DayForecast(1644597548, 5, 7,
            ForecastTemp(59.0F, 77.0F, 49.0F), 1023.0F, 100),
        DayForecast(1644683948, 6, 6,
            ForecastTemp(69.0F, 82.0F, 61.0F),1023.0F, 100),
        DayForecast(1644770348, 5, 8,
            ForecastTemp(70.0F, 81.0F, 60.0F), 1023.0F, 100),
        DayForecast(1644856748, 4, 6,
            ForecastTemp(75.0F, 88.0F, 62.0F), 1023.0F, 100),
        DayForecast(1644943148, 6, 7,
            ForecastTemp(83.0F, 93.0F, 65.0F), 1023.0F, 100),
        DayForecast(1645029548, 4, 6,
            ForecastTemp(82.0F, 95.0F, 59.0F), 1023.0F, 100),
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(adapterData)
    }
}