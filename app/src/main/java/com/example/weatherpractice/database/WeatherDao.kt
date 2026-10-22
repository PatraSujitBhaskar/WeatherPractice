package com.example.weatherpractice.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingSource
import com.example.weatherpractice.database.WeatherRemoteKey

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weather: List<WeatherEntity>)

    @Query("SELECT * FROM weather ORDER BY locationKey ASC")
    fun getAllWeather():Flow<List<WeatherEntity>>

    @Query("SELECT * FROM weather WHERE locationKey = :cityName LIMIT 1")
    suspend fun getWeatherByCity(cityName: String): WeatherEntity?

    @Query("DELETE FROM weather")
    suspend fun clearAll()

    @Query("SELECT * FROM weather ORDER BY locationKey ASC")
    fun getWeatherPagingSource(): PagingSource<Int, WeatherEntity>

    @Query("SELECT COUNT(*) FROM weather")
            suspend fun getWeatherCount(): Int
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(remoteKey: WeatherRemoteKey)

    @Query("SELECT * FROM weather_remote_keys WHERE id = 1")
    suspend fun getRemoteKey(): WeatherRemoteKey?

    @Query("DELETE FROM weather_remote_keys")
    suspend fun clearRemoteKeys()
}