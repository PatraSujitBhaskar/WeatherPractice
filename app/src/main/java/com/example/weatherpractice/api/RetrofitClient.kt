package com.example.weatherpractice.api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.open-meteo.com/"
    val api: WeatherAPI by lazy{
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(
            WeatherAPI::class.java)
    }
}
