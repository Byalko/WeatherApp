package com.example.weatherapp.data.model

import com.example.weatherapp.data.RecyclerViewSection
import com.example.weatherapp.data.RecyclerViewSectionDB
import com.example.weatherapp.data.WeatherDB
import com.example.weatherapp.data.WindDB

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

fun Weather.toWeatherDB() = WeatherDB(
    description = description,icon =  icon,main =  main
)