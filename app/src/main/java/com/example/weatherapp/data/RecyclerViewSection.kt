package com.example.weatherapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherapp.data.model.WeatherList
import com.example.weatherapp.data.model.toWeatherListDB
import com.example.weatherapp.db.WeatherListConverter

@Entity(tableName = "section")
data class RecyclerViewSectionDB(
    @PrimaryKey(autoGenerate = true)
    val sectionId: Int? = null,
    val label: String,
    @TypeConverters(WeatherListConverter::class)
    val items: List<WeatherListDB>
)

data class RecyclerViewSection(val label: String, val items: List<WeatherList>)

fun RecyclerViewSection.toRecyclerViewSectionDB() = RecyclerViewSectionDB(
    label = label,
    items = items.map { it.toWeatherListDB() }
)