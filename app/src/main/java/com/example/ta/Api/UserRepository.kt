package com.example.ta.Api

import com.example.ta.Model.Url_Volley
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserRepository {
    fun create(): UserService{
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Url_Volley.url_website+"/udemy/")
            .build()
        return  retrofit.create(UserService::class.java)
    }
}