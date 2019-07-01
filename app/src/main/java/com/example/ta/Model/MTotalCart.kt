package com.example.ta.Model

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MTotalCart{
    var sum = 0
    companion object{
        var total_cart:Int = 0
        var total_harga:Int = 0
        var total_berat:Int = 0
    }
}

//data class MTotalCart (var total_harga:Int, var total_cart:Int)