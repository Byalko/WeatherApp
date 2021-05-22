package com.example.weatherapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.RecyclerViewSectionDB

@Dao
interface WeatherDao {

    @Query("SELECT * FROM section")
    fun getAll(): List<RecyclerViewSectionDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(section: List<RecyclerViewSectionDB>)

    @Query("DELETE FROM section")
    suspend fun deleteAll()
}