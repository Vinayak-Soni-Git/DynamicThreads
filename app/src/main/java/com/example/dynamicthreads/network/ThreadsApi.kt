package com.example.dynamicthreads.network

import com.example.dynamicthreads.models.ThreadPublishRequest
import com.example.dynamicthreads.models.ThreadRequest
import com.example.dynamicthreads.models.ThreadResponse
import com.example.dynamicthreads.utils.Constants
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ThreadsApi {
    @Headers("Authorization: Bearer ${Constants.Long_LIVED_ACCESS_TOKEN}")
    @POST("v1.0/{userId}/threads")
    suspend fun createThread(@Path("userId") userId: String, @Body threadRequest: ThreadRequest): ThreadResponse

    @Headers("Authorization: Bearer ${Constants.Long_LIVED_ACCESS_TOKEN}")
    @POST("v1.0/{userId}/threads_publish")
    suspend fun publishThread(@Path("userId") userId: String, @Body threadPublishRequest: ThreadPublishRequest): ThreadResponse
}