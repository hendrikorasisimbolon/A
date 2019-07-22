package com.example.ta

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Adapter.DaftarKomplainAdapter
import com.example.ta.Fragment.NotFoundFragment
import com.example.ta.Model.MCart
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.Url_Volley
import com.example.ta.utilss.Tools
import kotlinx.android.synthetic.main.activity_daftar_komplain.*
import kotlinx.android.synthetic.main.ly_item_kom.view.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DaftarKomplainAct : AppCompatActivity(), DaftarKomplainAdapter.OnNoteListener {

    data class Terbeli(
        var created: String,
        var id_transdet: String,
        var judul_produk: String,
        var produk_id: String,
        var status: String
    )
    var list:ArrayList<Terbeli> = ArrayList()
    private lateinit var ui_hot: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_komplain)
        initToolbar()
        var url = Url_Volley.url_website +"/udemy/get_produk_terbeli.php?user_id="+ MCart.user_id
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var jar= JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->
            if(response.length()>0)
            {
                for(x in 0..response.length()-1)
                {
                    list.add(
                        Terbeli(
                            response.getJSONObject(x).getString("created"),
                            response.getJSONObject(x).getString("id_transdet"),
                            response.getJSONObject(x).getString("judul_produk"),
                            response.getJSONObject(x).getString("produk_id"),
                            response.getJSONObject(x).getString("status")
                        )
                    )
                }
                var adp= DaftarKomplainAdapter(this,list,this)
                rv_list_komplain.layoutManager = LinearLayoutManager(this)
                rv_list_komplain.adapter=adp
            }
            else {
                var frag = NotFoundFragment()
                var FM: FragmentManager? = supportFragmentManager
                var FT: FragmentTransaction = FM!!.beginTransaction()
                FT.replace(R.id.ly_k, frag)
                FT.commit()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(jar)


    }

    override fun onNoteClick(position: Int) {
        var intent = Intent(this, KomplainAct::class.java)
        intent.putExtra("id_produk", list[position].produk_id)
        intent.putExtra("id_transdet", list[position].id_transdet)
        startActivity(intent)
    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.getNavigationIcon()?.setColorFilter(resources.getColor(R.color.indigo_500), PorterDuff.Mode.SRC_ATOP)
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        toolbar.title = "Promo"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.white_transparency)
        Tools.setSystemBarLight(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart_setting, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.indigo_500))
        Tools.changeOverflowMenuIconColor(toolbar, resources.getColor(R.color.indigo_500))
        val menuItem: MenuItem = menu.findItem(R.id.aksi_cart)
        var actionView: View = MenuItemCompat.getActionView(menuItem)
        ui_hot = actionView.findViewById(R.id.hotlist_hot) as TextView
//        var i = Intent(this, OrderAct::class.java)
//        menuItem.intent = i
        setupBadge()

        actionView.setOnClickListener { onOptionsItemSelected(menuItem) }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
        }
        if (item.itemId == R.id.aksi_cart) {
            var i = Intent(this, OrderAct::class.java)
            startActivity(i)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupBadge() {

        if (ui_hot != null) {
            if (MTotalCart.total_cart.toInt() == 0) {
                if (ui_hot.getVisibility() !== View.GONE) {
                    ui_hot.setVisibility(View.GONE)
                }
            } else {
//                ui_hot.setText(String.valueOf(Math.min(mCartItemCount, 99)))
                ui_hot.setText(Math.min(MTotalCart.total_cart.toInt(), 99).toString())
                if (ui_hot.getVisibility() !== View.VISIBLE) {
                    ui_hot.setVisibility(View.VISIBLE)
                }
            }
        }
    }


}
