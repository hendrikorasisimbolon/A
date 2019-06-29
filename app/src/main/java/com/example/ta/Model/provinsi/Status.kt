package com.example.ta.Model.provinsi

/**
 * Created by Robby Dianputra on 2/12/2018.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class Status {

    @SerializedName("code")
    @Expose
    var code: Int? = null
    @SerializedName("description")
    @Expose
    var description: String? = null

}
