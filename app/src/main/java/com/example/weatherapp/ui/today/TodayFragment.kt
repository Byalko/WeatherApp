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
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.TodayFragmentBinding
import com.example.weatherapp.di.LocationUtils
import com.example.weatherapp.di.LocationUtils.isOnline
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task


class TodayFragment : Fragment(R.layout.today_fragment) {

    private val binding by viewBinding(TodayFragmentBinding::bind)
    private val viewModel: TodayViewModel by activityViewModels()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            requireActivity()
        )

        Log.d("Location", "OnCreate")

        binding.shareButton.setOnClickListener {
            if (isOnline(requireActivity(), requireContext())) {
                checkGPS()
            } else {
                Toast.makeText(requireContext(),"Нет сети", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.result.observe(viewLifecycleOwner,{
            with(binding){

                Glide.with(requireContext()).load(LocationUtils.DEFAULT_IMG+it.list[0]
                    .weather[0].icon + "@2x.png").centerCrop().into(iconWheat)
                val strCity = it.city.name+", "+ it.city.country
                city.text = strCity
                txtCompass.text = direction(it.list[0].wind.deg)

                gradusy.text="${it.list[0].main.temp.toInt()} °C | ${it.list[0].weather[0].main}"
                txtRainfall.text="${it.list[0].main.humidity}%"
                txtDegree.text="${it.list[0].main.pressure} hPa"
                txtWind.text="${((it.list[0].wind.speed)*3.6).toInt()} km/h"
            }
        })
    }

    private fun direction(deg: Int) : String {
        if(deg in 0..23 || deg in 339..360 )  return "N"
        if(deg in 24..68) return "NE"
        if(deg in 69..113) return "E"
        if(deg in 114..158) return "SE"
        if(deg in 158..203) return "S"
        if(deg in 204..248) return "SW"
        if(deg in 249..293) return "W"
        if(deg in 294..338) return "NW"
        else return "WWWW"
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