package com.example.ta.Model.provinsi

/**
 * Created by Robby Dianputra on 2/12/2018.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class Result(
    @field:SerializedName("province_id")
    @field:Expose
    var provinceId: String?, @field:SerializedName("province")
    @field:Expose
    var province: String?
)