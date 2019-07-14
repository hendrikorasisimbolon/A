package com.example.ta.Model

import com.google.gson.annotations.SerializedName


class ServerResponse {

    // variable name should be same as in the json response from php
    @SerializedName("status")
    var success: Boolean = false
    @SerializedName("message")
    var message: String? = null

}