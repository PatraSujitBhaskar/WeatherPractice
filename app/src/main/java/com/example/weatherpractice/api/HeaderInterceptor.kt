package com.example.weatherpractice.api

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedUrl = originalRequest.url.newBuilder().setQueryParameter("current", "temperature_2m,relative_humidity_2m").build()
        val modifiedRequest = originalRequest.newBuilder().url(modifiedUrl).header("X-App-Name", "WeatherPractice").build()
        return chain.proceed(modifiedRequest)
    }
}