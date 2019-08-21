package com.example.ta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.ta.Model.Url_Volley
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.utilss.Tools
import kotlinx.android.synthetic.main.activity_bukti_komplain.*
import kotlin.math.log

class BuktiKomplainAct : AppCompatActivity() {
    var ft = ""
    var ftt = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bukti_komplain)
        initToolbar()
        var id = intent.getStringExtra("idK")
        Log.e("idkom", id)


        var url = Url_Volley.url_website +"/udemy/get_success_komplain.php?user="+id
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var jar= JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->
            if(response.length()>0)
            {
                    ft =  response.getJSONObject(0).getString("foto_rek")
                    ftt =  response.getJSONObject(0).getString("foto_rek_type")

            }
            Glide.with(this).load(url_website+"/ecommerce/assets/images/komplain/"+ft+ftt)
                .into(foto_sukses)
            Log.e("foto komplain", url_website+"/ecommerce/assets/images/komplain/"+ft+ftt)
//            foto_sukses.setImageURI((url_website+"/ecommerce/assets/images/komplain/"+ft+ftt).toUri())
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(jar)

    }
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Bukti Pembayaran"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.grey_10)
        Tools.setSystemBarLight(this)
        toolbar.setNavigationOnClickListener{
            startActivity(Intent(this,DaftarKomplainAct::class.java))
        }
    }
}
