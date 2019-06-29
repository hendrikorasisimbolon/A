package com.example.ta.Model.kota

/**
 * Created by Robby Dianputra on 2/13/2018.
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
