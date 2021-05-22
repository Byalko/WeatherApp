package com.example.weatherapp.data.model

import com.example.weatherapp.data.WindDB

data class Wind(
    val deg: Int,
    val gust: Double,
    val speed: Double
)

fun Wind.toWindDB():WindDB {
    return WindDB(deg = this.deg,speed = this.speed)
}