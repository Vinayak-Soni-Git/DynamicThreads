package com.example.dynamicthreads.network

import com.example.dynamicthreads.models.TokenResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TokenService {
    @GET("access_token")
    fun getLongLivedToken(@Query("grant_type") grantType:String,
                          @Query("client_secret") clientSecret:String,
                          @Query("access_token") accessToken:String): Call<TokenResponse>
}