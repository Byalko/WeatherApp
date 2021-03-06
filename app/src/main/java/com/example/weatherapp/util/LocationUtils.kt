package com.example.weatherapp.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.vmadalin.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*

object LocationUtils {

    const val PERMISSION_LOCATION_REQUEST_CODE = 1
    const val DEFAULT_IMG = "http://openweathermap.org/img/wn/"

    fun parseDate(date:String, pattern:String): String {
        val parser =  SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.ENGLISH)
        val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
        return formatter.format(parser.parse(date)!!)
    }

    fun hasLocationPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    fun isOnline(activity:Activity, context: Context): Boolean {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            val connectivityManager =
                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        } else {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
            return false
        }

    }
}