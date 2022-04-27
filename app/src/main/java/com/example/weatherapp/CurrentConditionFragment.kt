package com.example.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
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

        viewModel.loadData(args.currentConditions)

        viewModel.navigateToForecast.observe(viewLifecycleOwner) {
            it?.let { coordinates -> navigateToForecast(coordinates) }
        }

        viewModel.viewState.observe(viewLifecycleOwner){
            bindData(it)
        }

        binding.forecastButton.setOnClickListener {
            viewModel.loadData()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.fragment_search)
        }
    }

    private fun navigateToForecast(coordinates: Coordinates) {
        val action = CurrentConditionFragmentDirections
            .actionCurrentConditionFragmentToForecastFragment(coordinates)
        findNavController().navigate(action)
    }

    private fun bindData(state: CurrentConditionViewModel.State) {
        binding.cityName.text = state.currentConditions?.name
        binding.temp.text = context?.getString(R.string.temp, state.currentConditions?.main?.temp?.toInt())
        binding.tempHigh.text = context?.getString(R.string.temp_high, state.currentConditions?.main?.tempMax?.toInt())
        binding.tempLow.text = context?.getString(R.string.temp_low, state.currentConditions?.main?.tempMin?.toInt())
        binding.tempHumid.text = context?.getString(R.string.temp_humid, state.currentConditions?.main?.humidity?.toInt())
        binding.tempPressure.text = context?.getString(R.string.temp_pressure, state.currentConditions?.main?.pressure?.toInt())
        binding.tempFeel.text = context?.getString(R.string.temp_feel,state. currentConditions?.main?.feelsLike?.toInt())
        val iconName = state.currentConditions?.weather?.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this).load(iconUrl).into(binding.tempIcon)
    }
}
