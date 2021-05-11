package com.example.weatherapp.ui.today

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.weatherapp.R
import com.example.weatherapp.databinding.TodayFragmentBinding
import com.example.weatherapp.di.LocationUtils.isOnline
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task


class TodayFragment : Fragment(R.layout.today_fragment) {

    private val binding by viewBinding(TodayFragmentBinding::bind)
    private val viewModel: TodayViewModel by activityViewModels()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            requireActivity()
        )

        Log.d("Location", "OnCreate")

        binding.btnLocation.setOnClickListener {
            if (isOnline(requireActivity(), requireContext())) {
                checkGPS()
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                Toast.makeText(
                    requireContext(),
                    "${location.latitude}  ${location.longitude}",
                    Toast.LENGTH_LONG
                ).show()
                Log.d("GPS_main", "Ok")

                viewModel.getData(location.latitude, location.longitude)
                stopLocationUpdates()
            } else {
                Log.d("GPS_main", "Error")
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
                requestNewLocationData()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods

        val mLocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5
        }

        Log.d("GPS_main", "Request")

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.getMainLooper()
        )

    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            //val location: Location = locationResult.lastLocation
            Log.d("GPS_main", "Callback")
            getLastLocation()
            //viewModel.getData(location.latitude, location.longitude)
        }
    }

    private fun checkGPS() {
        val locationRequest = LocationRequest.create()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> =
            settingsClient.checkLocationSettings(builder.build())

        task.addOnSuccessListener(requireActivity()) {
            Log.d("GPS_main", "OnSuccess")
            // GPS is ON
            getLastLocation()
        }

        task.addOnFailureListener(requireActivity()) { e ->
            Log.d("GPS_main", "GPS off")
            // GPS off
            if (e is ResolvableApiException) {
                try {
                    //e.startResolutionForResult(this@MainActivity, 111)
                    startIntentSenderForResult(e.resolution.intentSender, 111, null, 0, 0, 0, null)
                } catch (e1: IntentSender.SendIntentException) {
                    e1.printStackTrace()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111) {
            when (resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    requestNewLocationData()
                    Toast.makeText(requireContext(), "OK", Toast.LENGTH_SHORT).show()
                }
                AppCompatActivity.RESULT_CANCELED -> {
                    Toast.makeText(requireContext(), "Not enable", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "ELSE", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}