package com.example.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.FragmentCurrentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CurrentConditionFragment : Fragment() {
    private lateinit var binding: FragmentCurrentBinding
    @Inject lateinit var viewModel: CurrentConditionViewModel
    private val args: CurrentConditionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.title = "Current Condition"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.forecastButton.setOnClickListener {
            val action = CurrentConditionFragmentDirections.actionCurrentConditionFragmentToForecastFragment(args.zipCode)
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentCondition.observe(this) { currentCondition ->
            bindData(currentCondition)
        }
        viewModel.passData(args.zipCode)
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