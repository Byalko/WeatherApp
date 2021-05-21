package com.example.weatherapp.data.model

import com.example.weatherapp.data.*

data class WeatherList(
    val clouds: Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val pop: Double,
    val rain: Rain,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)

fun WeatherList.toWeatherListDB() = WeatherListDB(
    dt_txt = dt_txt,
    weather = weather.map { it.toWeatherDB() },
    main = main.toMainDB(),
    wind = wind.toWindDB()
)

