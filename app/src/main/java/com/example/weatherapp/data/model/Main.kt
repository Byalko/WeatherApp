package com.example.weatherapp.data.model

import com.example.weatherapp.data.MainDB
import com.example.weatherapp.data.WindDB

data class Main(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_kf: Double,
    val temp_max: Double,
    val temp_min: Double
)

fun Main.toMainDB(): MainDB {
    return MainDB(this.temp,this.humidity,this.pressure)
}