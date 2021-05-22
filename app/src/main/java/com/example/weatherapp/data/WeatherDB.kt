package com.example.weatherapp.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherapp.db.WeatherConverter

@Entity(tableName = "location")
class LocationDB(
    @PrimaryKey(autoGenerate = true)
    val locationId: Int? = null,
    val country: String,
    val name: String
)

@Entity(tableName = "weatherList")
data class WeatherListDB(
    @PrimaryKey(autoGenerate = true)
    val weatherListId: Int? = null,
    val dt_txt: String,
    @TypeConverters(WeatherConverter::class)
    val weather: List<WeatherDB>,
    @Embedded
    val main: MainDB,
    @Embedded
    val wind: WindDB
)

@Entity(tableName = "main")
data class MainDB(
    @PrimaryKey(autoGenerate = true)
    val mainId: Int? = null,
    val temp: Double,
    val humidity: Int,
    val pressure: Int
)

@Entity(tableName = "weather")
data class WeatherDB(
    @PrimaryKey(autoGenerate = true)
    val weatherId: Int? = null,
    val description: String,
    val icon: String,
    val main: String
)

@Entity(tableName = "wind")
data class WindDB(
    @PrimaryKey(autoGenerate = true)
    val windId: Int? = null,
    val deg: Int,
    val speed: Double
)