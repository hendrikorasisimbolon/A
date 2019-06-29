package com.example.ta.Model.kota

/**
 * Created by Robby Dianputra on 2/13/2018.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class Rajaongkir {

    @SerializedName("query")
    @Expose
    var query: Query? = null
    @SerializedName("status")
    @Expose
    var status: Status? = null
    @SerializedName("results")
    @Expose
    var results: List<Result>? = null

}
