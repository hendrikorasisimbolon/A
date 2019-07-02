package com.example.ta

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Fragment.QtyFragment
import com.example.ta.Model.MCart
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.utils.Tools
import com.example.ta.utils.ViewAnimation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.activity_product_detail.view.*
import java.text.NumberFormat
import java.util.*

class ProductDetailAct : AppCompatActivity() {

    var id : Int = 0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)


        var id_produk = intent.getStringExtra("id_produk").toString()
        var judul = intent.getStringExtra("judul_produk").toString()
        var harga:Double = intent.getStringExtra("harga_normal").toString().toDouble()
        var foto = intent.getStringExtra("foto").toString()
        var foto_type = intent.getStringExtra("foto_type").toString()
        var berat =intent.getStringExtra("berat").toString().toDouble()
        var deskripsi = intent.getStringExtra("deskripsi").toString()

        var url = url_website+"/udemy/get_rating.php?produk_id="+id_produk
        var rq:RequestQueue = Volley.newRequestQueue(this)
        var jor = JsonObjectRequest(Request.Method.GET,url,null,Response.Listener { response ->
            rt_barang.rating = response.getDouble("rating").toFloat()
            txt_bnykrt.text = response.getString("banyak")
        }, Response.ErrorListener { error ->
//            Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()

        })

        rq.add(jor)
        id = id_produk.toInt()

        var locale = Locale("in", "ID")
        var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)

        Picasso.with(image.context).load(url_website+"/ecommerce/assets/images/produk/"+foto+foto_type)
            .into(image.image)
        txt_judul_produk.text = judul
        price.text = formatRupiah.format(harga)
        text_kat.text = "Berat : "+ "%.0f".format(berat) + "gram"
        text_des.text = deskripsi

        initToolbar()
        initComponent()
    }


    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Fashion"
        Tools.setSystemBarColor(this)
    }
    private fun initComponent() {

        bt_toggle_description.setOnClickListener { view -> toggleSection(view, lyt_expand_description) }

        // expand first description
        toggleArrow(bt_toggle_description)
        lyt_expand_description.visibility = View.VISIBLE

        (findViewById<FloatingActionButton>(R.id.fab)).setOnClickListener {
            MCart.itemId = id
            val frag = QtyFragment()
            var mgr = (this as AppCompatActivity).fragmentManager
            frag.show(mgr, "Dlt")
        }
    }

    private fun toggleSection(bt: View, lyt: View) {
        val show = toggleArrow(bt)
        if (show) {
            ViewAnimation.expand(lyt, object : ViewAnimation.AnimListener {
                override fun onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt)
                }
            })
        } else {
            ViewAnimation.collapse(lyt)
        }
    }

    fun toggleArrow(view: View): Boolean {
        if (view.rotation == 0f) {
            view.animate().setDuration(200).rotation(180f)
            return true
        } else {
            view.animate().setDuration(200).rotation(0f)
            return false
        }
    }


}
