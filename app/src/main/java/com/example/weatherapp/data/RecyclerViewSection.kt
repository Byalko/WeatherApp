package com.example.weatherapp.data

import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.data.model.WeatherList
import com.example.weatherapp.data.model.toWeatherListDB

data class RecyclerViewSectionDB(val label: String, val items: List<WeatherListDB>)

data class RecyclerViewSection(val label: String, val items: List<WeatherList>)

fun RecyclerViewSection.toRecyclerViewSectionDB() = RecyclerViewSectionDB(
    label = label,
    items = items.map { it.toWeatherListDB() }
)