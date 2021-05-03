package com.example.weatherapp

import android.Manifest
import android.content.Context
import com.vmadalin.easypermissions.EasyPermissions

object LocationUtils {

    const val PERMISSION_LOCATION_REQUEST_CODE = 1

    fun hasLocationPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

}