package com.example.weatherpractice.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    val locationKey:String,
    val latitude:Double,
    val longitude: Double,
    val temperature:Double,
    val humidity:Int,
    val weatherCode:Int
)