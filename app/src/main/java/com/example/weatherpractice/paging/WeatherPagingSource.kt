package com.example.weatherpractice.paging
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.weatherpractice.api.RetrofitClient
import com.example.weatherpractice.model.IndianCities
import com.example.weatherpractice.model.WeatherResponse

class WeatherPagingSource : PagingSource<Int, WeatherResponse>(){
    override fun getRefreshKey(state: PagingState<Int, WeatherResponse>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1) ?: state.closestPageToPosition(
                position
            )?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WeatherResponse> {
        android.util.Log.d("WeatherPagingSource", "called method")
        val page = params.key?:0
        val pageSize = 10
        val cities = IndianCities.cities
        val startIndex =page * pageSize
        if(startIndex>=cities.size){
            return LoadResult.Page(data = emptyList(), prevKey=if(page==0)null else page-1, nextKey=null)
        }
        val endIndex = minOf(startIndex+pageSize, cities.size)
        val pageCities = cities.subList(startIndex, endIndex)
        return try{
            val latitude = pageCities.joinToString(","){it.latitude.toString()}
            val longitude = pageCities.joinToString(","){it.longitude.toString()}
            val response = RetrofitClient.api.getWeather(latitude=latitude, longitude=longitude)
            android.util.Log.d("WeatherPagingSource", "HTTP code: ${response.code()}")
            android.util.Log.d("WeatherPagingSource", "Successful: ${response.isSuccessful}")
            android.util.Log.d("WeatherPagingSource", "body: ${response.body()}")
            if(response.isSuccessful){
                val weatherData = response.body().orEmpty()
                android.util.Log.d("WeatherPagingSource", "Weather Items received: ${weatherData.size}")
                LoadResult.Page(data=weatherData, prevKey=if(page==0) null else page-1, nextKey = if(endIndex<cities.size)page+1 else null)
            }else{
                android.util.Log.e("WeatherPagingSource", "API error: ${response.code()} - ${response.errorBody()?.string()}")
                LoadResult.Error(Exception("API error: ${response.code()}"))
            }
        }catch(e: Exception){
            android.util.Log.e("WeatherPagingSource", "Exception:", e)
            LoadResult.Error(e)
        }
    }
}