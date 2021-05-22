package com.example.weatherapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.data.*

@Database(entities = [RecyclerViewSectionDB::class, WeatherListDB::class,
    MainDB::class, WeatherDB::class, WindDB::class, LocationDB::class], version = 2)
@TypeConverters(WeatherListConverter::class,WeatherConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDao():WeatherDao
    abstract fun locationDao():LocationDao
}