package com.example.homework17.network

import com.example.homework17.service.LoginService
import com.example.homework17.service.RegisterService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Network {

    private const val BASE_URL = "https://reqres.in/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()
    }

    fun registerService(): RegisterService = retrofit.create(RegisterService::class.java)

    fun loginService(): LoginService = retrofit.create(LoginService::class.java)
}