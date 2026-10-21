package com.example.weatherpractice

import com.example.weatherpractice.api.TokenAuthenticator
import com.example.weatherpractice.api.TokenProvider
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Test
class TokenAuthenticatorTest {
    @Test
    fun `refresh token after 401 and retry request`(){
        val server = MockWebServer()
        try{
            //First response: expired token is rejected
            server.enqueue(MockResponse().setResponseCode(401))
            //Second response: refreshed token is accepted.
            server.enqueue(MockResponse().setResponseCode(200))
            server.start()
            TokenProvider.updateTokens(newAccessToken = "expired_access_token", newRefreshToken = "valid_refresh_token")
            val client = OkHttpClient.Builder().addInterceptor { chain ->
                val token = TokenProvider.accessToken
                val request = chain.request().newBuilder().header("Authorization","Bearer $token").build()
                chain.proceed(request)
            }.authenticator(TokenAuthenticator()).build()
            val request = Request.Builder().url(server.url("/test")).build()
            client.newCall(request).execute().use{
                response -> assertEquals(200, response.code)
                //Confirm that the server received two requests
                val firstRequest = server.takeRequest()
                val secondRequest = server.takeRequest()
                assertEquals("Bearer expired_access_token", firstRequest.getHeader("Authorization"))
                assertEquals("Bearer refreshed_access_token_789", secondRequest.getHeader("Authorization"))

            }
        }finally {
            server.shutdown()
        }
    }
}