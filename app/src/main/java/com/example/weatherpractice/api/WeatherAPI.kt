package com.example.weatherpractice.api
import com.example.weatherpractice.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("v1/forecast")
    fun getWeather(
        @Query("latitude")latitude:Double,
        @Query("longitude")longitude: Double,
        @Query("current")current: String = "temperature_2m,relative_humidity_2m,weather_code"
    ): Call<WeatherResponse>
}