package com.example.weatherapp.ui.today

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.weatherapp.R
import com.example.weatherapp.di.LocationUtils.PERMISSION_LOCATION_REQUEST_CODE
import com.example.weatherapp.di.LocationUtils.hasLocationPermission
import com.example.weatherapp.databinding.TodayFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class TodayFragment : Fragment(R.layout.today_fragment), EasyPermissions.PermissionCallbacks {

    private val binding by viewBinding(TodayFragmentBinding::bind)

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        requestLocationPermission()
        Log.d("Location", "OnCreate")

        binding.btnLocation.setOnClickListener {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    Log.d("Location", location.latitude.toString())
                    Log.d("Location", location.longitude.toString())
                } else {
                    Log.d("Location", "Error")
                }
            }
        }

    }

    private fun requestLocationPermission() {
        if (hasLocationPermission(requireContext())) {
            return
        } else {
            EasyPermissions.requestPermissions(
                this,
                "This application cannot work without Location Permission.",
                PERMISSION_LOCATION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms.first())) {
            SettingsDialog.Builder(requireContext()).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}

}