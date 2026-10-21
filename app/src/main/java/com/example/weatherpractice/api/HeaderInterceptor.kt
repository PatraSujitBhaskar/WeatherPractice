package com.example.weatherpractice.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import android.util.Log
import com.example.weatherpractice.api.TokenProvider

class HeaderInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = TokenProvider.accessToken
        val originalRequest = chain.request()

        //Modify the request URL
        val modifiedUrl = originalRequest.url.newBuilder().setQueryParameter("forecast_days", "2").build()
        //Add a custom header
        val requestBuilder = originalRequest.newBuilder().url(modifiedUrl).header("X-App-Name", "WeatherPractice")
            .header("Accept-Language", "en-IN")
        if(!token.isNullOrBlank()){
            requestBuilder.header("Authorization", "Bearer $token")
        }else{
            requestBuilder.removeHeader("Authorization")
        }
        val modifiedRequest = requestBuilder.build()

        var attempt =0
        var maxAttempts=3
        while(true){
            attempt++
            try{
                Log.d("HeaderInterceptor", "Request attempt: $attempt")
                val response = chain.proceed(modifiedRequest)
                val modifiedResponse = response.newBuilder().header("X-App-Processed", "true").build()
                val bodySize = modifiedResponse.body?.contentLength()
                android.util.Log.d("HeaderInterceptor",
                    "custom response header: ${modifiedResponse.header("X-App-Processed")}")
                android.util.Log.d("HeaderInterceptor",
                    "response body size: $bodySize bytes")

                    //Return successful responses immediately
                if(response.isSuccessful)
                {
                    return modifiedResponse
                }
                // Retry only server error
                if(response.code !in 500..599 || attempt>= maxAttempts){
                    return modifiedResponse
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