package com.example.weatherapp.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.util.Resource
import com.example.weatherapp.data.RecyclerViewSection
import com.example.weatherapp.data.model.WeatherList
import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.di.LocationUtils
import com.example.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private var _result = MutableLiveData<WeatherModel>()
    val result: LiveData<WeatherModel> = _result

    private var _list = MutableLiveData<List<RecyclerViewSection>>()
    val list: LiveData<List<RecyclerViewSection>> = _list

    private val _result1 = MutableStateFlow<AllEvent>(AllEvent.Empty)
    val result1: StateFlow<AllEvent> = _result1

    fun getData(lat: Double, lon: Double) {

        viewModelScope.launch {
            _result1.value = AllEvent.Loading
            when (val response = weatherRepository.getFiveWeather(lat, lon)) {
                is Resource.Success -> {
                    _result1.value = AllEvent.Success(response.data!!)
                    _result.postValue(response.data)
                    withContext(Dispatchers.IO) { _list.postValue(inicializeSections(response.data.list)) }
                }
                is Resource.Error -> {
                    _result1.value = AllEvent.Failure(response.message.toString())
                }
            }
        }
    }

    private fun inicializeSections(weath: List<WeatherList>): MutableList<RecyclerViewSection> {
        var size = 0
        for (i in 1..7) {
            if (LocationUtils.parseDate(weath[0].dt_txt, "dd.MM.yyyy") == LocationUtils.parseDate(
                    weath[i].dt_txt,
                    "dd.MM.yyyy"
                )
            ) {
                size += 1
            }
        }

        val sections = mutableListOf<RecyclerViewSection>()
        val items1 = mutableListOf<WeatherList>()

        for (i in 0..size) {
            items1.add(weath[i])
        }
        val section1 = RecyclerViewSection("TODAY", items1)
        sections.add(section1)

        for (i in 1..4) {
            val items = mutableListOf<WeatherList>()

            for (ii in size + 1..size + 8) {
                items.add(weath[ii])
            }
            val dayOfWeek = LocationUtils.parseDate(weath[size + 8].dt_txt, "E")
            size += 8

            val section = RecyclerViewSection(dayOfWeek(dayOfWeek), items)
            sections.add(section)
        }
        return sections
    }

    private fun dayOfWeek(day: String): String {
        return when (day) {
            "Mon" -> "MONDAY"
            "Tue" -> "TUESDAY"
            "Wed" -> "WEDNESDAY"
            "Thu" -> "THURSDAY"
            "Fri" -> "FRIDAY"
            "Sat" -> "SATURDAY"
            else -> "SUNDAY"
        }
    }

    sealed class AllEvent {
        class Success(val result: WeatherModel) : AllEvent()
        class Failure(val errorText: String) : AllEvent()
        object Loading : AllEvent()
        object Empty : AllEvent()
    }
}