package com.example.ta.Model.cost

/**
 * Created by Robby Dianputra on 2/14/2018.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class Query {

    @SerializedName("key")
    @Expose
    var key: String? = null
    @SerializedName("origin")
    @Expose
    var origin: String? = null
    @SerializedName("destination")
    @Expose
    var destination: String? = null
    @SerializedName("weight")
    @Expose
    var weight: Int? = null
    @SerializedName("courier")
    @Expose
    var courier: String? = null

}
