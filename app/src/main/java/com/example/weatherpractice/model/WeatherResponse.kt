package com.example.weatherpractice.model

data class WeatherResponse(
    val latitude: Double,
    val longitude:Double,
    val current:CurrentWeather
)
data class CurrentWeather(
    val temperature_2m:Double,
    val relative_humidity_2m:Double,
    val weather_code:Int
)