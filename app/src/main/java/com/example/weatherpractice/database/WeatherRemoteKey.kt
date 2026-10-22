package com.example.weatherpractice.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_remote_keys")
data class WeatherRemoteKey(
    @PrimaryKey
    val id: Int = 1,
    val nextPage: Int
)