package com.example.weatherpractice.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.weatherpractice.api.RetrofitClient
import com.example.weatherpractice.database.WeatherDao
import com.example.weatherpractice.database.WeatherEntity
import com.example.weatherpractice.database.WeatherRemoteKey
import com.example.weatherpractice.database.toWeatherEntity
import com.example.weatherpractice.model.IndianCities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class WeatherRemoteMediator(
    private val weatherDao: WeatherDao
) : RemoteMediator<Int, WeatherEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, WeatherEntity>
    ): MediatorResult {

        return try {
            val cities = IndianCities.cities
            val pageSize = state.config.pageSize

            val page = when (loadType) {

                LoadType.REFRESH -> 0

                LoadType.PREPEND -> {
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }

                LoadType.APPEND -> {
                    val remoteKey = withContext(Dispatchers.IO) {
                        weatherDao.getRemoteKey()
                    }
                    Log.d("RemoteKeyCheck", "Retrieved nextPage=${remoteKey?.nextPage}")

                    remoteKey?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                }
            }

            val startIndex = page * pageSize

            Log.d(
                "RemoteMediator",
                "LoadType=$loadType, page=$page, startIndex=$startIndex"
            )

            if (startIndex >= cities.size) {
                return MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }

            val pageCities = cities.subList(
                startIndex,
                minOf(startIndex + pageSize, cities.size)
            )

            val latitude = pageCities.joinToString(",") {
                it.latitude.toString()
            }

            val longitude = pageCities.joinToString(",") {
                it.longitude.toString()
            }

            val response = RetrofitClient.api.getWeather(
                latitude = latitude,
                longitude = longitude
            )

            if (!response.isSuccessful) {
                return MediatorResult.Error(
                    Exception("Weather API error: ${response.code()}")
                )
            }

            val weatherData = response.body()
                ?: return MediatorResult.Error(
                    Exception("Weather API returned an empty response")
                )

            val entities = weatherData.map {
                it.toWeatherEntity()
            }

            withContext(Dispatchers.IO) {
                if (loadType == LoadType.REFRESH) {
                    weatherDao.clearAll()
                    weatherDao.clearRemoteKeys()
                }

                weatherDao.insertAll(entities)

                val nextPage = page + 1

                if (startIndex + pageCities.size < cities.size) {
                    weatherDao.insertRemoteKey(
                        WeatherRemoteKey(nextPage = nextPage)
                    )
                    Log.d("RemoteKeyCheck","Saved nextPage = $nextPage")
                } else {
                    weatherDao.clearRemoteKeys()
                }

                Log.d(
                    "RemoteMediator",
                    "Rows in Room = ${weatherDao.getWeatherCount()}"
                )
            }

            val endReached =
                startIndex + pageCities.size >= cities.size

            Log.d(
                "RemoteMediator",
                "Pagination ended=$endReached"
            )

            MediatorResult.Success(
                endOfPaginationReached = endReached
            )

        } catch (e: Exception) {
            Log.e("RemoteMediator", "Loading failed: $loadType", e)
            MediatorResult.Error(e)
        }
    }
}