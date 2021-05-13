package com.example.weatherapp.data

import com.example.weatherapp.data.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    companion object {
        const val BASE_URL = "http://api.openweathermap.org/"
    }

    @GET("data/2.5/forecast")
    suspend fun getFiveDay(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double
    ):Response<WeatherModel>
}