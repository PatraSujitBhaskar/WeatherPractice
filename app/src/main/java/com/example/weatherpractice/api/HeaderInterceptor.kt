package com.example.weatherpractice.api

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder().header("X-App-Name", "WeatherPractice").build()
        return chain.proceed(modifiedRequest)
    }
}