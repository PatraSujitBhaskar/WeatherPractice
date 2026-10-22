package com.example.weatherpractice.database

import android.content.Context
import androidx.room.Room

object WeatherDatabaseProvider{
    @Volatile
    private var INSTANCE:WeatherDatabase?= null

    fun getDatabase(context: Context): WeatherDatabase{
        return INSTANCE ?: synchronized(this){
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext, WeatherDatabase::class.java, "weather_database")
                .build().also{INSTANCE =it}
        }
    }
}