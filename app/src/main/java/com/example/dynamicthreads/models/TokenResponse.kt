package com.example.dynamicthreads.models

data class TokenResponse (
    val access_token:String,
    val token_type:String,
    val expires_in:Int
)