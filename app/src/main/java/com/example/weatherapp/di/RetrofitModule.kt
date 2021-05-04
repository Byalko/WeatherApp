package com.example.weatherapp.di

import com.example.weatherapp.data.WeatherAPI
import com.example.weatherapp.data.WeatherAPI.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun addApiKeyToRequests(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val originalHttpUrl = chain.request().url
        val newUrl = originalHttpUrl.newBuilder().addQueryParameter(
            "api_key", "0693b17c06c6556d4d5186dfad80a064"
        ).build()
        request.url(newUrl)
        return chain.proceed(request.build())
    }

    @Provides
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(provideLoggingInterceptor())
        addInterceptor { chain -> return@addInterceptor addApiKeyToRequests(chain) }
    }.build()

    @Singleton
    @Provides
    fun provideApiWeather(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(provideHttpClient())
        .build()

    @Singleton
    @Provides
    fun clientApiWeather(): WeatherAPI = provideApiWeather().create(WeatherAPI::class.java)
}