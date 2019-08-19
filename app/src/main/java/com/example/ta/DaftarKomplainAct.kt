package com.example.ta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Adapter.DKomplainAdapter
import com.example.ta.Fragment.NotFoundFragment
import com.example.ta.Model.MCart
import com.example.ta.Model.MDaftarKomplain
import com.example.ta.Model.Url_Volley
import com.example.ta.utilss.Tools
import kotlinx.android.synthetic.main.activity_daftar_komplain2.*

class DaftarKomplainAct : AppCompatActivity() {


    var list:ArrayList<MDaftarKomplain> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_komplain2)
        initToolbar()

        var url = Url_Volley.url_website +"/udemy/daftar_komplain.php?user="+ MCart.user_id
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var jar= JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->
            if(response.length()>0)
            {
                for(x in 0..response.length()-1)
                {
                    list.add(
                        MDaftarKomplain(
                            response.getJSONObject(x).getString("alasan"),
                            response.getJSONObject(x).getString("created_at"),
                            response.getJSONObject(x).getString("foto"),
                            response.getJSONObject(x).getString("foto_rek"),
                            response.getJSONObject(x).getString("foto_rek_type"),
                            response.getJSONObject(x).getString("foto_type"),
                            response.getJSONObject(x).getString("id_komplain"),
                            response.getJSONObject(x).getString("no_rek"),
                            response.getJSONObject(x).getString("selesai"),
                            response.getJSONObject(x).getString("status"),
                            response.getJSONObject(x).getString("trans_id"),
                            response.getJSONObject(x).getString("user")
                        )
                    )
                }
                var adp=DKomplainAdapter(this,list)
                rv_dftarkomplain.layoutManager = LinearLayoutManager(this)
                rv_dftarkomplain.adapter=adp
            }
            else {
                var frag = NotFoundFragment()
                var FM: FragmentManager? = supportFragmentManager
                var FT: FragmentTransaction = FM!!.beginTransaction()
                FT.replace(R.id.isi, frag)
                FT.commit()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(jar)

    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Daftar Komplain")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.grey_10)
        Tools.setSystemBarLight(this)
        toolbar.setNavigationOnClickListener{
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.grey_60))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else {
            Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }
}
