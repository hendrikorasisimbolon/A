package com.example.ta.Model.kota

/**
 * Created by Robby Dianputra on 2/13/2018.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class Query {

    @SerializedName("province")
    @Expose
    var province: String? = null

}
