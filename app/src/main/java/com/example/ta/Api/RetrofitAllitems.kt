package com.example.ta.Api

import com.example.ta.Model.Url_Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitAllitems {
    companion object{
        var retrofit:Retrofit?=null
        var base_url:String = "http://192.168.43.180/"
        fun get_client():Retrofit?{
            if (retrofit == null){
                var loggingInterceptor:HttpLoggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                var builder: OkHttpClient.Builder = OkHttpClient.Builder()
                builder.addInterceptor(loggingInterceptor)
                builder.connectTimeout(300,TimeUnit.SECONDS)
                builder.readTimeout(80,TimeUnit.SECONDS)
                builder.writeTimeout(90, TimeUnit.SECONDS)
                var gson:Gson = GsonBuilder().setLenient().create()
                retrofit = Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(builder.build())
                    .build()
            }
            return retrofit
        }
    }
}