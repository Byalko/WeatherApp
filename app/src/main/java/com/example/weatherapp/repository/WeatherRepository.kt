package com.example.weatherapp.repository

import com.example.weatherapp.util.Resource
import com.example.weatherapp.data.WeatherAPI
import com.example.weatherapp.data.model.WeatherModel
import retrofit2.Response
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherAPI
) {

    suspend fun getFiveWeather(lat:Double,lon:Double) : Resource<WeatherModel> = checkHttpResponse(api.getFiveDay(lat,lon))

    private fun <T> checkHttpResponse(response: Response<T>): Resource<T> {
        return try {
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}