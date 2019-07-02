package com.example.ta.Api

import android.content.Context
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

public object ApiProduct{
    val BASE_URL = url_website+"/udemy/"

    private var retrofit:Retrofit?=null

    var gson = GsonBuilder()
        .setLenient()
        .create()

    fun getRetrofitClient(context: Context):Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .build()

        if (retrofit==null){
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit!!
    }

}