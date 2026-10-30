package com.example.weatherpractice.domain.model


data class Weather(
    val locationKey:String,
    val latitude:Double,
    val longitude: Double,
    val temperature:Double,
    val humidity:Int,
    val weatherCode:Int
)