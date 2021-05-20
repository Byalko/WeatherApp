package com.example.weatherapp.data

import com.example.weatherapp.data.model.*
import com.example.weatherapp.data.model.Weather

class WeatherModelDB (
    val country: String,
    val name: String,
    val list: List<WeatherListDB>
)

data class WeatherListDB(
    val dt_txt: String,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind
)

data class MainDB(
    val temp: Double,
    val humidity: Int,
    val pressure: Int
)

data class WeatherDB(
    val description: String,
    val icon: String,
    val main: String
)

data class WindDB(
    val deg: Int,
    val speed: Double
)