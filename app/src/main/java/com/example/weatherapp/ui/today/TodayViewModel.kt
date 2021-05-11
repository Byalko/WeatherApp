package com.example.weatherapp.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    fun getData(lat:Double,lon:Double) {

        viewModelScope.launch {
            weatherRepository.getFiveWeather(lat, lon)
        }
    }
}