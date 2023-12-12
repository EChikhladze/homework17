package com.example.homework17.service

import com.example.homework17.common.User
import com.example.homework17.common.ServiceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {

    @POST("register")
    suspend fun register(@Body user: User): Response<ServiceResponse>
}