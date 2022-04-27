package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.ErrorDialogFragment.Companion.TAG
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var binding: FragmentSearchBinding
    @Inject lateinit var viewModel: SearchViewModel
    private lateinit var permissionRequest: ActivityResultLauncher<Array<String>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.title = "Search"
        return binding.root
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.enableButton.observe(viewLifecycleOwner) { enable ->
            binding.submitButton.isEnabled = enable
        }

        viewModel.showErrorDialog.observe(viewLifecycleOwner) { showError ->
            if (showError) {
                ErrorDialogFragment().show(childFragmentManager, TAG)
            }
        }

        viewModel.currentCondition.observe(viewLifecycleOwner) { current ->
            val currentCondition: CurrentCondition = current
            val action = SearchFragmentDirections
                .actionSearchFragmentToCurrentConditionFragment(currentCondition)
            findNavController().navigate(action)
        }

        permissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){ permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    requestLocationUpdates()
                }
                else -> {
                    print("No location access granted")
                }
            }
        }

        binding.zipCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.updateZipCode(p0.toString())
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.submitButton.setOnClickListener {
            try {
                viewModel.loadZip()
            } catch (e: retrofit2.HttpException) {
                ZipErrorDialogFragment().show(childFragmentManager, TAG)
            }
        }

        binding.locationButton.setOnClickListener {
            requestLocationRationale()
            try {
                viewModel.loadLatLon()
            } catch (e: retrofit2.HttpException) {
                ErrorDialogFragment().show(childFragmentManager, TAG)
            }
        }

        binding.bellButton.setOnClickListener{
            binding.notificationButton.visibility = View.VISIBLE
            binding.notificationButton.text = context?.getString(R.string.notificationOn)
        }

        var flag = true
        binding.notificationButton.setOnClickListener {
            if (flag) {
                flag = false
                requestNotificationPermission()
                requireContext().startService(Intent(requireContext(), LocalService::class.java))
                binding.notificationButton.text = context?.getString(R.string.notificationOff)
            } else {
                flag = true
                binding.notificationButton.visibility = View.GONE
            }
        }
    }

    private fun requestLocationRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this.requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            AlertDialog.Builder(this.requireContext()).setTitle("Request")
                .setTitle("Allow this app access to your location?")
                .setNeutralButton("Ok") { _, _ ->
                    permissionRequest.launch(
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
                }.show()
        } else {
            permissionRequest.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val locationRequest = LocationRequest.create()
        locationRequest.interval = 10000L
        locationRequest.fastestInterval = 5000L
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val locationProvider = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach{
                     viewModel.updateLatLong(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                }
            }
        }
        locationProvider.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper())

        locationProvider.lastLocation.addOnSuccessListener {
            Log.d("SearchFragment", it.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun requestNotificationPermission(){
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(), Manifest.permission.FOREGROUND_SERVICE) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(), Manifest.permission.FOREGROUND_SERVICE) !=
            PackageManager.PERMISSION_GRANTED) {
            requestBackgroundPermissionRationale()
        } else {
            print("No permission granted.")
            return
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun requestBackgroundPermissionRationale(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this.requireActivity(), Manifest.permission.FOREGROUND_SERVICE)) {
            AlertDialog.Builder(this.requireContext()).setTitle("Request")
                .setTitle("Allow this app run in the background?")
                .setNeutralButton("Ok") { _, _ ->
                    permissionRequest.launch(
                        arrayOf(Manifest.permission.FOREGROUND_SERVICE))
                }.show()
        } else {
            permissionRequest.launch(arrayOf(Manifest.permission.FOREGROUND_SERVICE))
        }
    }
}