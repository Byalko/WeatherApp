package com.example.weatherapp.repository

import com.example.weatherapp.data.LocationDB
import com.example.weatherapp.data.RecyclerViewSectionDB
import com.example.weatherapp.db.LocationDao
import com.example.weatherapp.db.WeatherDao
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val weatherDao: WeatherDao,
    private val locationDao: LocationDao
) {

    fun getAll() = weatherDao.getAll()
    suspend fun deleteAll() = weatherDao.deleteAll()
    suspend fun insertAll(sectionDB: List<RecyclerViewSectionDB>) = weatherDao.insertAll(sectionDB)

    fun getLocation() = locationDao.getAll()
    suspend fun insertLocation(location: LocationDB) = locationDao.insert(location)
    suspend fun deleteLocation() = locationDao.deleteAll()
}