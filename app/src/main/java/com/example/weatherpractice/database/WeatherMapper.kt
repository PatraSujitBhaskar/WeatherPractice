package com.example.weatherpractice.database

import com.example.weatherpractice.model.WeatherResponse
fun WeatherResponse.toWeatherEntity(): WeatherEntity{
    return WeatherEntity(locationKey = "${latitude}_${longitude}",
        latitude=latitude, longitude=longitude,
        temperature = current.temperature_2m, humidity = current.relative_humidity_2m.toInt(), weatherCode = current.weather_code)
}