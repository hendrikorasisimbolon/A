package com.example.ta.Model.provinsi


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlin.Result


public class Rajaongkir {

    @SerializedName("query")
    @Expose
    var query: List<Any>? = null
    @SerializedName("status")
    @Expose
    var status: Status? = null
    @SerializedName("results")
    @Expose
    var results: List<com.example.ta.Model.provinsi.Result>? = null

}

