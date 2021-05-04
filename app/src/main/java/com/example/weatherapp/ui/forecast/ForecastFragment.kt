package com.example.weatherapp.ui.forecast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ForecastFragmentBinding

class ForecastFragment : Fragment(R.layout.forecast_fragment) {

    private val binding by viewBinding(ForecastFragmentBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}