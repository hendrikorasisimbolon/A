package com.example.ta

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Adapter.ItemAdapter
import com.example.ta.Fragment.ErrorFragment
import com.example.ta.Model.MCart
import com.example.ta.Model.MItemDetail
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.MTotalCart.Companion.total_cart
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.utils.Tools
import kotlinx.android.synthetic.main.action_bar_notifitcation_icon.*
import kotlinx.android.synthetic.main.activity_items.*
import kotlinx.android.synthetic.main.toolbar.*


public class ItemsAct : AppCompatActivity(), ItemAdapter.OnNoteListener {

    var list = ArrayList<MItemDetail>()
    private lateinit var ui_hot:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        initToolbar()

//        var cat:String=intent.getStringExtra("cat")
//        var url= url_website+"/udemy/get_subkat.php?subkat_id="+cat  /// intent get ekstra nya rusak

        if (ProductCatAct.cat != "0")
        {
            var url= url_website+"/udemy/get_subkat.php?subkat_id="+ProductCatAct.cat

            var rq: RequestQueue = Volley.newRequestQueue(this)

            var jar = JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->

                for(x in 0..response.length()-1)
                {
                    list.add(
                        MItemDetail(
                            response.getJSONObject(x).getInt("id_produk"),
                            response.getJSONObject(x).getString("judul_produk"),
                            response.getJSONObject(x).getDouble("harga_normal"),
                            response.getJSONObject(x).getString("deskripsi"),
                            response.getJSONObject(x).getDouble("berat"),
                            response.getJSONObject(x).getString("foto"),
                            response.getJSONObject(x).getString(("foto_type")),
                            response.getJSONObject(x).getDouble("harga_diskon"),
                            response.getJSONObject(x).getDouble("stok"),
                            response.getJSONObject(x).getDouble("diskon"),
                            response.getJSONObject(x).getDouble("subkat_id"),
                            response.getJSONObject(x).getDouble("kat_id")
                        )
                    )
                }

                var adp=ItemAdapter(this,list,this)
                item_rv.layoutManager= LinearLayoutManager(this)
                item_rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                item_rv.adapter=adp


            },Response.ErrorListener { error ->
                Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()

            })
            rq.add(jar)

            item_rv.layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )

        }
        else
        {
            var frag = ErrorFragment()
            var FM: androidx.fragment.app.FragmentManager? = supportFragmentManager
            var FT: FragmentTransaction = FM!!.beginTransaction()
            FT.replace(R.id.halaman, frag)
            FT.commit()
        }



//        var url1 = url_website+"/udemy/get_total_cart.php?user_id="+ MCart.user_id
//        var rq1: RequestQueue = Volley.newRequestQueue(this)
//        var jor = JsonObjectRequest(Request.Method.GET,url1,null, Response.Listener { response ->
//            //            cart_size.text = response.getInt("banyak").toString()
//            Log.e("Banyak Cart", response.getString("banyak"))
//            MTotalCart.total_cart = response.getInt("banyak")
//        }, Response.ErrorListener { error ->
//            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
//        })
//        rq1.add(jor)

    }

    override fun onResume() {
        getcart()
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        getcart()
        super.onCreate(savedInstanceState, persistentState)
    }

    fun getcart()
    {
        var url = url_website +"/udemy/get_total_cart.php?user_id="+MCart.user_id
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var jor = JsonObjectRequest(Request.Method.GET,url,null, Response.Listener { response ->
            //            cart_size.text = response.getInt("banyak").toString()
            Log.e("Banyak Cart", response.getString("banyak"))
            total_cart = response.getInt("banyak")
            MTotalCart.total_harga = response.getInt("jumlah")
            MTotalCart.total_berat = response.getInt("berat")
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(jor)
        hotlist_hot?.text = Math.min(total_cart,99).toString()
    }



    override fun onNoteClick(position: Int) {
        var intent = Intent(this, ProductDetailAct::class.java)
        intent.putExtra("id_produk", list.get(position).id.toString())
        intent.putExtra("judul_produk", list.get(position).judul_produk.toString())
        intent.putExtra("harga_normal", list.get(position).harga_normal.toString())
        intent.putExtra("foto", list.get(position).foto.toString())
        intent.putExtra("foto_type", list.get(position).foto_type.toString())
        intent.putExtra("berat", list.get(position).berat.toString())
        intent.putExtra("deskripsi",list.get(position).deksripsi.toString())

        startActivity(intent)
    }
    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.getNavigationIcon()?.setColorFilter(resources.getColor(R.color.indigo_500), PorterDuff.Mode.SRC_ATOP)
        toolbar.setNavigationOnClickListener{
            var i = Intent(this,MainActivity::class.java)
            startActivity(i)
        }
        toolbar.title = "Profile"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.white_transparency)
        Tools.setSystemBarLight(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart_setting_search, menu)
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
        if (item.itemId == R.id.aksi_cart)
        {
            var i = Intent(this, OrderAct::class.java)
            startActivity(i)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupBadge() {

        if (ui_hot != null) {
            if (total_cart.toInt() == 0) {
                if (ui_hot.getVisibility() !== View.GONE) {
                    ui_hot.setVisibility(View.GONE)
                }
            } else {
//                ui_hot.setText(String.valueOf(Math.min(mCartItemCount, 99)))
                ui_hot.setText(Math.min(total_cart.toInt(),99).toString())
                if (ui_hot.getVisibility() !== View.VISIBLE) {
                    ui_hot.setVisibility(View.VISIBLE)
                }
            }
        }
    }

}
