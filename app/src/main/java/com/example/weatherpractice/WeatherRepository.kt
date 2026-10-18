package com.example.weatherpractice

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.weatherpractice.model.WeatherResponse
import com.example.weatherpractice.paging.WeatherPagingSource
import kotlinx.coroutines.flow.Flow

class WeatherRepository {
    fun getWeather(): Flow<PagingData<WeatherResponse>>{
        return Pager(config = PagingConfig(pageSize = 10, initialLoadSize = 10, enablePlaceholders = false), pagingSourceFactory = {
            WeatherPagingSource()
        }).flow
    }
}
