package com.example.ta.Api

import com.example.ta.Model.MItems
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

public interface ApiServiceProduct
{
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("get_all_product.php")
    public fun getProducts(): Call<List<MItems>>

}