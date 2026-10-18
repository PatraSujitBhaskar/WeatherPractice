package com.example.weatherpractice.api
import com.example.weatherpractice.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface WeatherAPI {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude")latitude:String,
        @Query("longitude")longitude:String,
        @Query("current")current: String = "temperature_2m,relative_humidity_2m,weather_code"
    ): Response<List<WeatherResponse>>
}
