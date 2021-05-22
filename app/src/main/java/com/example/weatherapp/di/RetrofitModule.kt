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
            "appid", "67901cf7da944ab97cf90b4656ad27b4"
        ).build()
        request.url(newUrl)
        return chain.proceed(request.build())
    }

    @Provides
    fun provideHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(httpLoggingInterceptor)
        addInterceptor { chain -> return@addInterceptor addApiKeyToRequests(chain) }
    }.build()

    @Singleton
    @Provides
    fun provideApiWeather(httpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(httpClient)
        .build()

    @Singleton
    @Provides
    fun clientApiWeather(retrofit: Retrofit): WeatherAPI = retrofit.create(WeatherAPI::class.java)
}