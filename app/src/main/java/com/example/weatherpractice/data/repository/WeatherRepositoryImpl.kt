package com.example.weatherpractice.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.weatherpractice.database.WeatherDao
import com.example.weatherpractice.database.WeatherEntity
import com.example.weatherpractice.domain.repository.WeatherRepository
import com.example.weatherpractice.paging.WeatherRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val weatherDao: WeatherDao) : WeatherRepository{
    @OptIn(ExperimentalPagingApi::class)
    override fun getWeather(): Flow<PagingData<WeatherEntity>> {
        return Pager(config = PagingConfig(pageSize = 10, initialLoadSize = 10, prefetchDistance = 1, enablePlaceholders = false),
            remoteMediator = WeatherRemoteMediator(weatherDao), pagingSourceFactory = {
                weatherDao.getWeatherPagingSource()
            }).flow
    }
}