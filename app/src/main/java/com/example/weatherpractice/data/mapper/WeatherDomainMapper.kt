package com.example.weatherpractice.data.mapper
import com.example.weatherpractice.domain.model.Weather
import com.example.weatherpractice.database.WeatherEntity

fun WeatherEntity.toDomain(): Weather{
    return Weather(locationKey=locationKey, latitude=latitude, longitude=longitude, temperature=temperature,
        humidity=humidity, weatherCode=weatherCode)
}