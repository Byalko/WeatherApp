package com.example.weatherapp.data

class WeatherModelDB(
    val country: String,
    val name: String,
    val list: List<WeatherListDB>
)

data class WeatherListDB(
    val dt_txt: String,
    val weather: List<WeatherDB>,
    val main: MainDB,
    val wind: WindDB
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