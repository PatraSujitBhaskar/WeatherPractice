package com.example.weatherpractice.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import android.util.Log

class HeaderInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        //Modify the request URL
        val modifiedUrl = originalRequest.url.newBuilder().setQueryParameter("current", "temperature_2m,relative_humidity_2m").build()
        //Add a custom header
        val modifiedRequest = originalRequest.newBuilder().url(modifiedUrl).header("X-App-Name", "WeatherPractice").build()

        var attempt =0
        var maxAttempts=3
        while(true){
            attempt++
            try{
                Log.d("HeaderInterceptor", "Request attempt: $attempt")
                val response = chain.proceed(modifiedRequest)
                //Return successful responses immediately
                if(response.isSuccessful)
                {
                    return response
                }
                // Retry only server error
                if(response.code !in 500..599 || attempt>= maxAttempts){
                    return response
                }
                Log.d("HeaderInterceptor", "Server error ${response.code}.Retrying...")
                response.close()
            }catch(e:IOException){
                if (attempt>=maxAttempts){
                    throw e
                }
                Log.d("HeaderInterceptor", "Network error. Retrying: ${e.message}")
            }
        }
    }
}