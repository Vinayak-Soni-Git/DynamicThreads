package com.example.dynamicthreads.models

data class ThreadRequest(
    val media_type: String = "TEXT",
    val text: String
)