package com.example.weatherapp.data

import androidx.room.Entity

class LocationDB(
    val country: String,
    val name: String
)

@Entity(tableName = "weatherList")
data class WeatherListDB(
    val dt_txt: String,
    val weather: List<WeatherDB>,
    val main: MainDB,
    val wind: WindDB
)

@Entity(tableName = "main")
data class MainDB(
    val temp: Double,
    val humidity: Int,
    val pressure: Int
)

@Entity(tableName = "weather")
data class WeatherDB(
    val description: String,
    val icon: String,
    val main: String
)

@Entity(tableName = "wind")
data class WindDB(
    val deg: Int,
    val speed: Double
)