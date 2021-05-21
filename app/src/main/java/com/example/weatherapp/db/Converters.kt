package com.example.weatherapp.db

import androidx.room.TypeConverter
import com.example.weatherapp.data.WeatherDB
import com.example.weatherapp.data.WeatherListDB
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType

class Converters {

    private val moshi = Moshi.Builder().build()
    private val listMyData : ParameterizedType = Types.newParameterizedType(List::class.java, WeatherDB::class.java)
    private val list : ParameterizedType = Types.newParameterizedType(List::class.java, WeatherListDB::class.java)
    private val jsonAdapter: JsonAdapter<List<WeatherDB>> = moshi.adapter(listMyData)
    private val jsonAdapter2: JsonAdapter<List<WeatherListDB>> = moshi.adapter(list)

    @TypeConverter
    fun weatherDBToStr(listMyModel: List<WeatherDB>?): String? {
        return jsonAdapter.toJson(listMyModel)
    }

    @TypeConverter
    fun strToWeatherDB(jsonStr: String?): List<WeatherDB>? {
        return jsonStr?.let { jsonAdapter.fromJson(jsonStr) }
    }

    @TypeConverter
    fun weatherListDBToStr(listMyModel: List<WeatherListDB>?): String? {
        return jsonAdapter2.toJson(listMyModel)
    }

    @TypeConverter
    fun strToWeatherListDB(jsonStr: String?): List<WeatherListDB>? {
        return jsonStr?.let { jsonAdapter2.fromJson(jsonStr) }
    }
}