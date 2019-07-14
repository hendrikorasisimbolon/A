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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ta.Adapter.RatingAdapter
import com.example.ta.Model.MCart
import com.example.ta.Model.MRatingBarang
import com.example.ta.Model.MTotalCart
import com.example.ta.Model.Url_Volley
import com.example.ta.utilss.Tools
import kotlinx.android.synthetic.main.action_bar_notifitcation_icon.*
import kotlinx.android.synthetic.main.activity_rekomendasi.*
import kotlinx.android.synthetic.main.toolbar.*

class RekomendasiAct : AppCompatActivity(),RatingAdapter.OnNoteListener {

    var li = ArrayList<MRatingBarang>()


    private lateinit var ui_hot: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rekomendasi)
        initToolbar()

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))

        swipeRefreshLayout.isRefreshing = false

        swipeRefreshLayout.setOnRefreshListener {
            li.clear()
            getProducts(li)
            getcart()
            swipeRefreshLayout.isRefreshing = false
        }
        li.clear()
        getProducts(li)

    }


    fun getProducts(li: ArrayList<MRatingBarang> ) {
        MRatingBarang.daftarRating = MRatingBarang.getAlgo(this)
        var wqe = MRatingBarang.daftarRating.sortedBy { it.rating }
        var wqa = MRatingBarang.list
        var asw  = MRatingBarang.list
        for (i in 0..wqa.count()-1)
        {
            for(j in 0..wqe.count()-1)
            {
                if(wqe[j].item==wqa[i].id)
                {
                    wqa[i].rating = wqe[j].rating
                    li.add(wqa[i])
                }
            }
        }
        for (i in 0..li.count()-1)
        {
            asw.remove(li[i])
        }
        li.addAll(asw.distinct())
        val so: List<MRatingBarang> = li.sortedWith(compareByDescending{it.rating})
        li.clear()
        if (so.count()>10)
        {
            for(i in 0..10)
            {
                li.add(so[i])
            }
        }
        else
        {
            li.addAll(so)
        }

        var adp= RatingAdapter(this, li,this)
        rt_rating.layoutManager= LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rt_rating.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        rt_rating.adapter=adp

    }

    override fun onNoteClick(position: Int) {
        var intent = Intent(this, ProductDetailAct::class.java)
        intent.putExtra("id_produk", li.get(position).id.toString())
        intent.putExtra("judul_produk", li.get(position).judul_produk.toString())
        intent.putExtra("harga_normal", li.get(position).harga_normal.toString())
        intent.putExtra("foto", li.get(position).foto.toString())
        intent.putExtra("foto_type", li.get(position).foto_type.toString())
        intent.putExtra("berat", li.get(position).berat.toString())
        intent.putExtra("deskripsi",li.get(position).deksripsi.toString())

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
