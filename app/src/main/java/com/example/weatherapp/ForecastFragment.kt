package com.example.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.FragmentForecastBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForecastFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentForecastBinding
    @Inject lateinit var viewModel: ForecastViewModel
    private val args: ForecastFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForecastBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.title = "Forecast"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView.findViewById(R.id.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)
    }

    override fun onResume() {
        super.onResume()
        viewModel.forecast.observe(this){ forecast ->
            binding.recyclerView.adapter = Adapter(forecast.list)
        }
        viewModel.passData(args.zipCode)
        viewModel.loadData()
    }
}