package com.example.ta.Api


import com.example.ta.Model.MCart
import com.example.ta.Model.MCart.Companion.user_id

import com.example.ta.Model.UserInfo
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @GET("get_user.php")
//    fun getUser(@QueryName id:String):Call<List<UserInfo>>
    fun getUser(@Query("id") id:String):Call<List<UserInfo>>
//    fun getUser(): Call<List<UserInfo>>
}