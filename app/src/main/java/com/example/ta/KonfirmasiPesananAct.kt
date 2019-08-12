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
import kotlinx.android.synthetic.main.activity_konfirmasi_pesanan.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class KonfirmasiPesananAct : AppCompatActivity() {

    companion object{
        var bayarsebelum:ArrayList<WaktuPembayaran> = ArrayList()
    }

    data class WaktuPembayaran(
        var waktu:Date,
        var id_trans:String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirmasi_pesanan)

        initToolbar()

        val sdf = SimpleDateFormat("yyyy:MM:dd:HH:mm")
        val currentDateandTime = sdf.format(Date())

        val date = sdf.parse(currentDateandTime)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, 1)


        var ongkir = intent.getStringExtra("ongkir").toString()
        var serv = intent.getStringExtra("service").toString()
        var kurir = intent.getStringExtra("kurir").toString()

        var url1 = Url_Volley.url_website +"/udemy/konfir.php?user_id="+MCart.user_id+"&kurir="+kurir+"&service="+serv+"&ongkir="+ongkir
        var rq1: RequestQueue = Volley.newRequestQueue(this)
        var jor = StringRequest(Request.Method.GET,url1, Response.Listener { response ->
            Toast.makeText(this,response.toString(), Toast.LENGTH_LONG).show()
            bayarsebelum.add(
                WaktuPembayaran(calendar.time,response.toString())
            )
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
            Log.e("errorKOnfrimasi", error.message)
        })
        rq1.add(jor)
        btn_order.setOnClickListener {
            startActivity(Intent(this,RiwayatAct::class.java))
        }
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
