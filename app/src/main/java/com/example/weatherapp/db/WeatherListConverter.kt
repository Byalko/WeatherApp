package com.example.weatherapp.db

import androidx.room.TypeConverter
import com.example.weatherapp.data.WeatherListDB
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType

class WeatherListConverter {
    private val moshi = Moshi.Builder().build()

    private val list: ParameterizedType =
        Types.newParameterizedType(List::class.java, WeatherListDB::class.java)
    private val jsonAdapter: JsonAdapter<List<WeatherListDB>> = moshi.adapter(list)

    @TypeConverter
    fun weatherListDBToStr(listMyModel: List<WeatherListDB>?): String? {
        return jsonAdapter.toJson(listMyModel)
    }

    @TypeConverter
    fun strToWeatherListDB(jsonStr: String?): List<WeatherListDB>? {
        return jsonStr?.let { jsonAdapter.fromJson(jsonStr) }
    }
}