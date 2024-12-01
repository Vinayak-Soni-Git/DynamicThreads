package com.example.dynamicthreads.network

import com.example.dynamicthreads.models.ThreadPublishRequest
import com.example.dynamicthreads.models.ThreadRequest
import com.example.dynamicthreads.models.ThreadResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ThreadsApi {
    @Headers("Authorization: Bearer THQWJXNV90R3FFdFdxdzg2eFY5NEVucXh1bFZAhLUpGZAHd0N3JxX3ZAuUVNKQnN1bE5GeUdQZAi1Cc2R4TUNJdjNHREJYSVJ5bExyTlhqSk0zZAkFxaXJyWkdmc1U3cER2bTRWb1dmVTViMTgzenlGSEM3Y0drbVRaQnF6cDhhbnZApMzBiTWRFSzhnT1YxUkxRRUJ4WWcZD")
    @POST("v1.0/{userId}/threads")
    suspend fun createThread(@Path("userId") userId: String, @Body threadRequest: ThreadRequest): ThreadResponse

    @Headers("Authorization: Bearer THQWJXNV90R3FFdFdxdzg2eFY5NEVucXh1bFZAhLUpGZAHd0N3JxX3ZAuUVNKQnN1bE5GeUdQZAi1Cc2R4TUNJdjNHREJYSVJ5bExyTlhqSk0zZAkFxaXJyWkdmc1U3cER2bTRWb1dmVTViMTgzenlGSEM3Y0drbVRaQnF6cDhhbnZApMzBiTWRFSzhnT1YxUkxRRUJ4WWcZD")
    @POST("v1.0/{userId}/threads_publish")
    suspend fun publishThread(@Path("userId") userId: String, @Body threadPublishRequest: ThreadPublishRequest): ThreadResponse
}