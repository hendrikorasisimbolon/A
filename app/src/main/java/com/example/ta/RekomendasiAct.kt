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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Adapter.RatingAdapter
import com.example.ta.Fragment.NotFoundFragment
import com.example.ta.Model.*
import com.example.ta.Model.Url_Volley.Companion.url_server
import com.example.ta.Model.Url_Volley.Companion.url_website
import com.example.ta.utilss.Tools
import kotlinx.android.synthetic.main.action_bar_notifitcation_icon.*
import kotlinx.android.synthetic.main.activity_rekomendasi.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeoutException

@Suppress("DEPRECATION")
class RekomendasiAct : AppCompatActivity(),RatingAdapter.OnNoteListener {

    var li = ArrayList<MRatingBarang>()

    private lateinit var ui_hot: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rekomendasi)
        initToolbar()
        get_prediksi()

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))
        MRatingBarang.daftarRating = MRatingBarang.getAlgo(this)
        swipeRefreshLayout.isRefreshing = false

        swipeRefreshLayout.setOnRefreshListener {
//            getProducts()
            li.clear()
            get_prediksi()
            getcart()
            swipeRefreshLayout.isRefreshing = false
        }
//        try {
//            getProducts()
////            MRatingBarang.daftarRating = MRatingBarang.getAlgo(this)
//        }catch (error:InterruptedException){
//            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
//        }catch (error: ExecutionException){
//            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
//        }catch (error: TimeoutException){
//            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
//        }
    }


//    fun getProducts() {
////        var wqe = MRatingBarang.getAlgo(this)
////        var li = ArrayList<MRatingBarang>()
////        li.clear()
////        li = get_prediksi()
//        Log.e("DataRek", li.size.toString())
//        if(li.size > 0 )
//        {
//            var adp= RatingAdapter(this, li,this)
//            rt_rating.layoutManager= LinearLayoutManager(this)
//            rt_rating.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
//            rt_rating.adapter=adp
//
//        }
//    }

    fun get_prediksi() {
    //        var or = ArrayList<MRatingBarang>()
        var url= Url_Volley.url_website +"/udemy/get_prediksi.php?id="+MCart.user_id

        var rq: RequestQueue = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(Request.Method.GET,url,null, Response.Listener { response ->
            if  (response.length()>0) {
                for (x in 0..response.length() - 1) {
                    Log.e("Prediksi", response.getJSONObject(x).getString("prediksi"))
                    li.add(
                        MRatingBarang(
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
                            response.getJSONObject(x).getString("foto_type"),
                            response.getJSONObject(x).getDouble("prediksi"),
                            "(A)"
                        )
                    )
                }
                var adp = RatingAdapter(this, li, this)
                rt_rating.layoutManager = LinearLayoutManager(this)
                rt_rating.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                rt_rating.adapter = adp
            }
            else {
                var frag = NotFoundFragment()
                var FM: FragmentManager? = supportFragmentManager
                var FT: FragmentTransaction = FM!!.beginTransaction()
                FT.replace(R.id.halaman_id, frag)
                FT.commit()
            }

        }, Response.ErrorListener { error ->
//            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
            Toast.makeText(this,"Harap rating barang terlebih dahulu!", Toast.LENGTH_LONG).show()

        })
        rq.add(jar)
//        return or
    }

    override fun onNoteClick(position: Int) {
        var intent = Intent(this, ProductDetailAct::class.java)
        intent.putExtra("id_produk", li.get(position).id.toString())
        intent.putExtra("judul_produk", li.get(position).judul_produk)
        intent.putExtra("harga_normal", li.get(position).harga_normal.toString())
        intent.putExtra("foto", li.get(position).foto)
        intent.putExtra("foto_type", li.get(position).foto_type)
        intent.putExtra("judul_kat", li.get(position).judul_kat)
        intent.putExtra("judul_subkat", li.get(position).judul_subkat)
        intent.putExtra("stok", li.get(position).stok.toString())
        intent.putExtra("kat_id", li.get(position).kat_id.toString())
        intent.putExtra("subkat_id", li.get(position).subkat_id.toString())
        intent.putExtra("berat", li.get(position).berat.toString())
        intent.putExtra("deskripsi",li.get(position).deksripsi.toString())
        intent.putExtra("discount",li.get(position).diskon.toString())
        intent.putExtra("harga_diskon", li.get(position).harga_diskon.toString())


        startActivity(intent)

    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.navigationIcon?.setColorFilter(resources.getColor(R.color.indigo_500), PorterDuff.Mode.SRC_ATOP)
        toolbar.setNavigationOnClickListener{
            var i = Intent(this,MainActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i)
        }
        toolbar.title = "Rekomendasi"
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
        if (item.itemId == R.id.aksi_cart)
        {
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
        var url = Url_Volley.url_website +"/udemy/get_total_cart.php?user_id="+ MCart.user_id
        var rq: RequestQueue = Volley.newRequestQueue(this)
        var jor = JsonObjectRequest(Request.Method.GET,url,null, Response.Listener { response ->
            //            cart_size.text = response.getInt("banyak").toString()
            Log.e("Banyak Cart", response.getString("banyak"))
            MTotalCart.total_cart = response.getInt("banyak")
            MTotalCart.total_harga = response.getInt("jumlah")
            MTotalCart.total_berat = response.getInt("berat")
        }, Response.ErrorListener { error ->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        rq.add(jor)
        hotlist_hot?.text = Math.min(MTotalCart.total_cart,99).toString()
    }

}
