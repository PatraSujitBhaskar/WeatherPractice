package com.example.weatherpractice.api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.Authenticator



object RetrofitClient {
    private const val BASE_URL = "https://api.open-meteo.com/"
    private val loggingInterceptor = HttpLoggingInterceptor().apply{
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor())
        .authenticator(TokenAuthenticator())
        .addInterceptor(loggingInterceptor).build()
    val api: WeatherAPI by lazy{

        Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build().create(WeatherAPI::class.java)
    }
}
