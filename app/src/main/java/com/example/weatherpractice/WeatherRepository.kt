package com.example.weatherpractice

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.weatherpractice.model.WeatherResponse
import com.example.weatherpractice.paging.WeatherPagingSource
import kotlinx.coroutines.flow.Flow
import com.example.weatherpractice.database.WeatherDatabaseProvider
import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.example.weatherpractice.database.WeatherDao
import com.example.weatherpractice.database.WeatherEntity
import com.example.weatherpractice.paging.WeatherRemoteMediator
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


class WeatherRepository @Inject constructor(private val weatherDao: WeatherDao) {
    @OptIn(ExperimentalPagingApi::class)
    fun getWeather(): Flow<PagingData<WeatherEntity>>{
        return Pager(config = PagingConfig(pageSize = 10, initialLoadSize = 10, prefetchDistance = 1, enablePlaceholders = false),
            remoteMediator = WeatherRemoteMediator(weatherDao), pagingSourceFactory = {
            weatherDao.getWeatherPagingSource()
        }).flow
    }
}
