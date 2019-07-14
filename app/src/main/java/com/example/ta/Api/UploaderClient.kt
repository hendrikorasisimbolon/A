package com.example.ta.Api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object UploaderClient{
    private var retrofit:Retrofit?=null

    private val BASE_URL = "http://192.168.43.180/"

    fun getRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(UploaderClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}