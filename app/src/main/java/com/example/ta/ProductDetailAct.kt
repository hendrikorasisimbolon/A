package com.example.ta

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.ta.Adapter.TestiAdapter
import com.example.ta.Fragment.NotFoundFragment
import com.example.ta.Fragment.QtyFragment
import com.example.ta.KategoriAct.Companion.cat
import com.example.ta.KategoriAct.Companion.catName
import com.example.ta.Model.MCart
import com.example.ta.Model.MTesti
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.utilss.Tools
import com.example.ta.utilss.ViewAnimation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.haozhang.lib.SlantedTextView
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.activity_product_detail.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductDetailAct : AppCompatActivity() {

    var id : Int = 0
    var stok = 0.0
    var list:ArrayList<MTesti> = ArrayList()
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        val nf = NumberFormat.getNumberInstance()
        val ng = NumberFormat.getNumberInstance()
        nf.maximumFractionDigits = 0
        ng.maximumFractionDigits = 2
        var id_produk = intent.getStringExtra("id_produk").toString()
        var judul = intent.getStringExtra("judul_produk").toString()
        var harga:Double = intent.getStringExtra("harga_diskon").toString().toDouble()
        var foto = intent.getStringExtra("foto").toString()
        var foto_type = intent.getStringExtra("foto_type").toString()
        var berat =intent.getStringExtra("berat").toString().toDouble()
        var deskripsi = intent.getStringExtra("deskripsi").toString()
        var discount = intent.getStringExtra("discount").toString().toDouble()
        var harga_normal = intent.getStringExtra("harga_normal").toDouble()
        var jdl_subkat = intent.getStringExtra("judul_subkat")
        var jdl_kat = intent.getStringExtra("judul_kat")
        var subkat_id = intent.getStringExtra("subkat_id")
        var kat_id = intent.getStringExtra("kat_id")
        stok = intent.getStringExtra("stok").toDouble()

        if(foto==""){
            foto = "not_found_img"
        }
        if(foto_type==""){
            foto_type=".jpg"
        }
//        Glide.with(image.context).load(url_website+"/ecommerce/assets/images/produk/not_found_img.jpg")
//            .into(image.image)
        var url = url_website+"/udemy/get_rating.php?produk_id="+id_produk
        var rq:RequestQueue = Volley.newRequestQueue(this)
        var jor = JsonObjectRequest(Request.Method.GET,url,null,Response.Listener { response ->
            rt_barang.rating = response.getDouble("rating").toFloat()
            txt_bnykrt.text = response.getString("banyak") +" (" + ng.format(response.getDouble("rating")).toString() +")"
        }, Response.ErrorListener { error ->
//            Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
        })

        rq.add(jor)
        id = id_produk.toInt()

        var locale = Locale("in", "ID")
        var formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(locale)
        if(foto!=""&&foto_type!=""||foto!=null&&foto_type!=null){
            Glide.with(image.context).load(url_website+"/ecommerce/assets/images/produk/"+foto+foto_type)
                .into(image.image)
        }
//        else if(foto==""&&foto_type==""){
////            image.setImageResource(R.drawable.not_found_img)
//            Glide.with(image.context).load(url_website+"/ecommerce/assets/images/produk/not_found_img.jpg")
//                .into(image.image)
//        }
        txt_judul_produk.text = judul
        price.text = formatRupiah.format(harga)
        text_brt.text = "Berat : "+ "%.0f".format(berat) + " kilogram"
        txt_des.text = Html.fromHtml(deskripsi, Html.FROM_HTML_MODE_COMPACT)

        if(discount>0){
            pricesb.text = formatRupiah.format(harga_normal)
            pricesb.paintFlags = STRIKE_THRU_TEXT_FLAG
            stvDiscountD.setText("Disc."+nf.format(discount)+" %")
                .setTextColor(Color.WHITE)
                .setSlantedLength(70).mode = SlantedTextView.MODE_RIGHT
        }
        else
        {
            pricesb.visibility = View.INVISIBLE
            stvDiscountD.visibility = View.INVISIBLE
        }

        if(stok>0){
            text_tersedia.text = "Stok Tersedia"
            text_tersedia.setTextColor(Color.GREEN)
        }
        text_kat.text = jdl_kat+" > "+jdl_subkat
        text_kat.setOnClickListener {
            cat = subkat_id
            catName = jdl_subkat
            val intent = Intent(this, ItemsAct::class.java)
            startActivity(intent)
        }
        getTesti(id_produk)
        initToolbar()
        initComponent()


    }

    fun getTesti(id_produk:String) {
        var url = url_website + "/udemy//testi.php?item_id="+id_produk
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(Request.Method.GET, url, null, Response.Listener { response ->
            if(response.length() > 0)
            {
                for(x in 0..response.length()-1)
                {
                    list.add(
                        MTesti(
                            response.getJSONObject(x).getString("date_crate"),
                            response.getJSONObject(x).getString("name"),
                            response.getJSONObject(x).getString("photo"),
                            response.getJSONObject(x).getString("photo_type"),
                            response.getJSONObject(x).getDouble("rating").toFloat(),
                            response.getJSONObject(x).getString("testi")
                        )
                    )
                }
                var adp = TestiAdapter(this, list)
                list_testi.layoutManager = LinearLayoutManager(this)
                list_testi.adapter = adp
            }
            else
            {
                var frag = NotFoundFragment()
                var FM: FragmentManager? = supportFragmentManager
                var FT: FragmentTransaction = FM!!.beginTransaction()
                FT.replace(R.id.lyt_expand_testimoni, frag)
                FT.commit()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(jar)
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

        bt_toggle_testi.setOnClickListener { view ->
            toggleSection(view, lyt_expand_testimoni)
        }
        toggleArrow(bt_toggle_testi)
        lyt_expand_testimoni.visibility = View.VISIBLE

        (findViewById<FloatingActionButton>(R.id.fab)).setOnClickListener {
            MCart.itemId = id
            if(stok > 0)
            {
                val frag = QtyFragment()
                var mgr = (this as AppCompatActivity).fragmentManager
                frag.show(mgr, "Dlt")
            }
            else{
                Toast.makeText(this, "Stok Barang Habis!", Toast.LENGTH_LONG).show()
            }

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
