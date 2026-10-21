package com.example.weatherpractice.api

import android.util.Log
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator : Authenticator{
    override fun authenticate(route: Route?, response: Response): Request? {
        //prevent an infinite retry loop
        if(responseCount(response)>=2){
            Log.e("TokenAuthenticator", "Authentication failed after retry")
            return null
        }
        val refreshToken =TokenProvider.refreshToken
        if(refreshToken.isNullOrBlank()){
            Log.e("TokenAuthenticator", "Refresh token is missing")
            return null
        }
        //simulate a successful token refresh for learning purposes
        val newAccessToken = "refreshed_access_token_789"
        TokenProvider.updateTokens(newAccessToken=newAccessToken, newRefreshToken = refreshToken)
        //Retry the original request with the new access token
        return response.request.newBuilder().header("Authorization", "Bearer $newAccessToken").build()
    }

    private fun responseCount(response: Response):Int{
        var count =1
        var priorResponse = response.priorResponse
        while(priorResponse!=null){
            count++
            priorResponse = priorResponse.priorResponse
        }
        return count
    }
}