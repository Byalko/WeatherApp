package com.example.weatherapp.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.LocationDB
import com.example.weatherapp.data.RecyclerViewSection
import com.example.weatherapp.data.RecyclerViewSectionDB
import com.example.weatherapp.data.model.WeatherList
import com.example.weatherapp.data.toRecyclerViewSectionDB
import com.example.weatherapp.util.LocationUtils
import com.example.weatherapp.repository.DatabaseRepository
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private var _location = MutableLiveData<LocationDB>()
    val location: LiveData<LocationDB> = _location

    private val _list = MutableStateFlow<AllEvent>(AllEvent.Empty)
    val list: StateFlow<AllEvent> = _list

    init {
        viewModelScope.launch {
            val localWeather = withContext(Dispatchers.IO) { databaseRepository.getAll() }
            val localLocation = withContext(Dispatchers.IO) { databaseRepository.getLocation() }
            if (localWeather.isNotEmpty() && localLocation.isNotEmpty()) {
                _list.value = AllEvent.Success(localWeather)
                _location.postValue(localLocation[0])
            }
        }
    }

    fun getData(lat: Double, lon: Double) {

        viewModelScope.launch {
            _list.value = AllEvent.Loading

            when (val response = weatherRepository.getFiveWeather(lat, lon)) {
                is Resource.Success -> {
                    if (response.data != null) {
                        withContext(Dispatchers.IO) { databaseRepository.deleteLocation() }
                        withContext(Dispatchers.IO) {
                            val remoteLocation = LocationDB(
                                country = response.data.city.country,
                                name = response.data.city.name
                            )
                            databaseRepository.insertLocation(remoteLocation)
                            _location.postValue(remoteLocation)
                        }
                        val remoteWeather = response.data.list
                        val localWeather = withContext(Dispatchers.Default) {
                            inicializeSections(remoteWeather).map { it.toRecyclerViewSectionDB() }
                        }
                        _list.value = AllEvent.Success(localWeather)
                        withContext(Dispatchers.IO) { databaseRepository.deleteAll() }
                        withContext(Dispatchers.IO) { databaseRepository.insertAll(localWeather) }
                    } else {
                        _list.value = AllEvent.Failure("No data")
                    }
                }
                is Resource.Error -> {
                    _list.value = AllEvent.Failure(response.message.toString())
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
        class Success(val result: List<RecyclerViewSectionDB>) : AllEvent()
        class Failure(val errorText: String) : AllEvent()
        object Loading : AllEvent()
        object Empty : AllEvent()
    }
}