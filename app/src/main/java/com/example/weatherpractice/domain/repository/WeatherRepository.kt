package com.example.weatherpractice.domain.repository

import androidx.paging.PagingData
import com.example.weatherpractice.database.WeatherEntity
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeather(): Flow<PagingData<WeatherEntity>>
}