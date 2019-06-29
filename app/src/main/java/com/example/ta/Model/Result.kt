package com.example.ta.Model

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("products")
    var products: List<MItems> = listOf()
)