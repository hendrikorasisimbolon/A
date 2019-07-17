package com.example.ta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Model.MCart
import com.example.ta.Model.Url_Volley
import com.example.ta.utilss.Tools

class KonfirmasiPesananAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirmasi_pesanan)

        initToolbar()

        var ongkir = intent.getStringExtra("ongkir").toString()
        var serv = intent.getStringExtra("service").toString()
        var kurir = intent.getStringExtra("kurir").toString()

        var url1 = Url_Volley.url_website +"/udemy/konfir.php?user_id="+MCart.user_id+"&kurir="+kurir+"&service="+serv+"&ongkir="+ongkir
        var rq1: RequestQueue = Volley.newRequestQueue(this)
        var jor = StringRequest(Request.Method.GET,url1, Response.Listener { response ->
            Toast.makeText(this,response.toString(), Toast.LENGTH_LONG).show()
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
            Log.e("errorKOnfrimasi", error.message)
        })
        rq1.add(jor)
    }
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title=""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.grey_10)
        Tools.setSystemBarLight(this)
        toolbar.setNavigationOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}
