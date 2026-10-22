package com.example.weatherpractice.database

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(
    entities = [WeatherEntity::class,
        WeatherRemoteKey::class],
    version = 2,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase(){
    abstract fun weatherDao(): WeatherDao
}