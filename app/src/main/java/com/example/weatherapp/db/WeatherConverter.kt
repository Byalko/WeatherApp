package com.example.weatherapp.db

import androidx.room.TypeConverter
import com.example.weatherapp.data.WeatherDB
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType

class WeatherConverter {

    private val moshi = Moshi.Builder().build()
    private val listMyData: ParameterizedType =
        Types.newParameterizedType(List::class.java, WeatherDB::class.java)
    private val jsonAdapter: JsonAdapter<List<WeatherDB>> = moshi.adapter(listMyData)

    @TypeConverter
    fun weatherDBToStr(listMyModel: List<WeatherDB>?): String? {
        return jsonAdapter.toJson(listMyModel)
    }

    @TypeConverter
    fun strToWeatherDB(jsonStr: String?): List<WeatherDB>? {
        return jsonStr?.let { jsonAdapter.fromJson(jsonStr) }
    }
}