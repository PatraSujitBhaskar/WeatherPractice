package com.example.weatherpractice.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import android.util.Log

class HeaderInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        //Modify the request URL
        val modifiedUrl = originalRequest.url.newBuilder().setQueryParameter("forecast_days", "2").build()
        //Add a custom header
        val modifiedRequest = originalRequest.newBuilder().url(modifiedUrl).header("X-App-Name", "WeatherPractice")
            .header("Accept-Language", "en-IN").build()
        Log.d("HeaderInterceptor", "Sending request: ${modifiedRequest.url}")
        val response = chain.proceed(modifiedRequest)
        Log.d("HeaderInterceptor", "Response code: ${response.code}")
        return response
    }
}