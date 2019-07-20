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
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Adapter.ItemAdapter
import com.example.ta.Api.ApiServiceProduct
import com.example.ta.Model.MCart
import com.example.ta.Model.MItemDetail
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.utilss.Tools
import kotlinx.android.synthetic.main.action_bar_notifitcation_icon.*
import kotlinx.android.synthetic.main.activity_product_all.*
import kotlinx.android.synthetic.main.toolbar.*

class ProductAllAct : AppCompatActivity(),ItemAdapter.OnNoteListener {

    private lateinit var apiServiceProduct: ApiServiceProduct
    private lateinit var productAdp: ItemAdapter
    var list = ArrayList<MItemDetail>()
    private lateinit var ui_hot: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_all)
        setSupportActionBar(toolbar)
        initToolbar()


        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))

        swipeRefreshLayout.isRefreshing = false

        swipeRefreshLayout.setOnRefreshListener {
            list.clear()
            getProducts(list)
            getcart()
            swipeRefreshLayout.isRefreshing = false
        }

        // assign a layout manager to the recycler view
//        products_recyclerview.layoutManager = StaggeredGridLayoutManager(
//            2,
//            StaggeredGridLayoutManager.VERTICAL
//        )
        list.clear()
        getProducts(list)

//
//        var url = url_website+"/udemy/get_total_cart.php?user_id="+ MCart.user_id
//        var rq: RequestQueue = Volley.newRequestQueue(this)
//        var jor = JsonObjectRequest(Request.Method.GET,url,null, com.android.volley.Response.Listener { response ->
//            //            cart_size.text = response.getInt("banyak").toString()
//            Log.e("Banyak Cart", response.getString("banyak"))
//            MTotalCart.total_cart = response.getInt("banyak")
//        }, com.android.volley.Response.ErrorListener { error ->
//            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
//        })
//        rq.add(jor)

    }


    fun getProducts(list:ArrayList<MItemDetail>) {
        var url= url_website+"/udemy/get_all_product.php"

        var rq: RequestQueue = Volley.newRequestQueue(this)

        var jar = JsonArrayRequest(Request.Method.GET,url,null, com.android.volley.Response.Listener { response ->

            for(x in 0..response.length()-1)
            {
                list.add(
                    MItemDetail(
                        response.getJSONObject(x).getInt("id_produk"),
                        response.getJSONObject(x).getString("judul_produk"),
                        response.getJSONObject(x).getDouble("harga_normal"),
                        response.getJSONObject(x).getDouble("harga_diskon"),
                        response.getJSONObject(x).getDouble("diskon"),
                        response.getJSONObject(x).getString("deskripsi"),
                        response.getJSONObject(x).getDouble("berat"),
                        response.getJSONObject(x).getDouble("stok"),
                        response.getJSONObject(x).getDouble("kat_id"),
                        response.getJSONObject(x).getString("judul_kategori"),
                        response.getJSONObject(x).getDouble("subkat_id"),
                        response.getJSONObject(x).getString("judul_subkategori"),
                        response.getJSONObject(x).getString("foto"),
                        response.getJSONObject(x).getString(("foto_type"))
                    )
                )
            }

            var adp=ItemAdapter(this,list,this)
            products_recyclerview.layoutManager= LinearLayoutManager(this)
            products_recyclerview.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            products_recyclerview.adapter=adp


        }, com.android.volley.Response.ErrorListener { error ->
            Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()

        })
        rq.add(jar)
    }

    override fun onNoteClick(position: Int) {
        var intent = Intent(this, ProductDetailAct::class.java)
        intent.putExtra("id_produk", list.get(position).id.toString())
        intent.putExtra("judul_produk", list.get(position).judul_produk)
        intent.putExtra("harga_normal", list.get(position).harga_normal.toString())
        intent.putExtra("foto", list.get(position).foto)
        intent.putExtra("foto_type", list.get(position).foto_type)
        intent.putExtra("judul_kat", list.get(position).judul_kat)
        intent.putExtra("judul_subkat", list.get(position).judul_subkat)
        intent.putExtra("stok", list.get(position).stok.toString())
        intent.putExtra("kat_id", list.get(position).kat_id.toString())
        intent.putExtra("subkat_id", list.get(position).subkat_id.toString())
        intent.putExtra("berat", list.get(position).berat.toString())
        intent.putExtra("deskripsi",list.get(position).deksripsi.toString())
        intent.putExtra("discount",list.get(position).diskon.toString())
        intent.putExtra("harga_diskon", list.get(position).harga_diskon.toString())



        startActivity(intent)

    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setColorFilter(resources.getColor(R.color.indigo_500), PorterDuff.Mode.SRC_ATOP)
        toolbar.setNavigationOnClickListener{
            var i = Intent(this,MainActivity::class.java)
            startActivity(i)
        }
        toolbar.title = "All Product"
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
        if (item.itemId == R.id.action_search)
        {
            var i = Intent(this, ResultSearchAct::class.java)
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
                ui_hot.setText(Math.min(MTotalCart.total_cart.toInt(),99).toString())
                if (ui_hot.getVisibility() !== View.VISIBLE) {
                    ui_hot.setVisibility(View.VISIBLE)
                }
            }
        }
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
        var jor = JsonObjectRequest(Request.Method.GET,url,null, com.android.volley.Response.Listener { response ->
            //            cart_size.text = response.getInt("banyak").toString()
            Log.e("Banyak Cart", response.getString("banyak"))
            MTotalCart.total_cart = response.getInt("banyak")
            MTotalCart.total_harga = response.getInt("jumlah")
            MTotalCart.total_berat = response.getInt("berat")
        }, com.android.volley.Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(jor)
        hotlist_hot?.text = Math.min(MTotalCart.total_cart,99).toString()
    }



}
