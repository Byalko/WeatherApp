package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.btn_location)
        button.setOnClickListener {
            requestPermission()
            if (hasLocationPermissionGranted()){
                Toast.makeText(this,"Permission granted",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun hasLocationPermissionGranted() =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED


    private fun requestPermission() {
        val permissionToRequest = mutableListOf<String>()
        if(!hasLocationPermissionGranted()) {
            permissionToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if(permissionToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionToRequest.toTypedArray(), 0)
        }
    }
}