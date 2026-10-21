package com.example.weatherpractice.api

import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

object PostRequestExample {
    fun createRequest(): Request{
        //creating a json request body
        val json = """
        {
            "city":"Bangalore",
            "units":"Celcius"
        }""".trimIndent()
        //converts JSON into an OkHttp RequestBody
        val body = json.toRequestBody("application/json;charset=utf-8".toMediaType())
        return Request.Builder()
            .url("https://httpbin.org/post")
            .post(body)
            .build()
    }
    fun executeRequest(){
        val client = okhttp3.OkHttpClient.Builder().addInterceptor(PostBodyInterceptor()).build()
        val request = createRequest()
        client.newCall(request).enqueue(object:okhttp3.Callback{

            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                android.util.Log.e("PostRequestExample", "Request failed: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.use{
                    android.util.Log.d("PostRequestExample","HTTP status: ${it.code}")
                    android.util.Log.d("PostRequestExample","Server response: ${it.body?.string()}")

                }
            }
        })
    }
}