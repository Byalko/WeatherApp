package com.example.weatherapp.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private var _result = MutableLiveData<WeatherModel>()
    val result : LiveData<WeatherModel> = _result

    fun getData(lat:Double,lon:Double) {

        viewModelScope.launch {
            val response = weatherRepository.getFiveWeather(lat, lon)
            if (response.data !=null){
                _result.postValue(response.data!!)
            }
        }
    }
}