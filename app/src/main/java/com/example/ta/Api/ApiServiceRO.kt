package com.example.ta.Api

import com.example.ta.Model.cost.ItemCost
import com.example.ta.Model.kota.ItemCity
import com.example.ta.Model.provinsi.ItemProvince
import retrofit2.Call
import retrofit2.http.*

interface ApiServiceRO {
    // Province
    @GET("province")
    @Headers("key: 28a1281c220ead530dfe7438d6e62146")
    abstract fun getProvince(): Call<ItemProvince>

    // City
    @GET("city")
    @Headers("key: 4abd53cc25919616cfc9a21ae55168f1")
    abstract fun getCity(@Query("province") province: String): Call<ItemCity>

    // Cost
    @FormUrlEncoded
    @POST("cost")
    abstract fun getCost(
        @Field("key") Token: String,
        @Field("origin") origin: String,
        @Field("destination") destination: String,
        @Field("weight") weight: String,
        @Field("courier") courier: String
    ): Call<ItemCost>
}