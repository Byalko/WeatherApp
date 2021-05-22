package com.example.weatherapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.data.LocationDB

@Dao
interface LocationDao {

    @Query("SELECT * FROM location")
    fun getAll(): List<LocationDB>

    @Insert
    suspend fun insert(location: LocationDB)

    @Query("DELETE FROM location")
    suspend fun deleteAll()

}