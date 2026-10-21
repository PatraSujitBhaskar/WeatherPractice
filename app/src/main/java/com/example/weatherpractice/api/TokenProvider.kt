package com.example.weatherpractice.api

object TokenProvider {
    @Volatile
    var accessToken: String?= "expired_access_token"
    @Volatile
    var refreshToken: String?= "valid_refresh_token"
    @Synchronized
    fun updateTokens(newAccessToken:String, newRefreshToken:String?=refreshToken){
        accessToken=newAccessToken
        refreshToken=newRefreshToken
    }
}