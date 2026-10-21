package com.example.weatherpractice.api
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType

class PostBodyInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if(originalRequest.method != "POST"){
            return chain.proceed(originalRequest)
        }
        val originalBody = originalRequest.body?: return chain.proceed(originalRequest)
        val buffer = okio.Buffer()
        originalBody.writeTo(buffer)
        val originalJson = buffer.readUtf8()
        val modifiedJson = originalJson.replace("\"Bangalore","\"Delhi")
        val modifiedBody = modifiedJson.toRequestBody("application/json;charset=utf-8".toMediaType())
        val modifiedRequest = originalRequest.newBuilder().method("POST", modifiedBody).build()
        android.util.Log.d("PostBodyInterceptor", "Modified Json: $modifiedJson")
        return chain.proceed(modifiedRequest)
    }
}