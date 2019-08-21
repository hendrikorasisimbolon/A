//package com.example.ta
//
//import android.content.Intent
//import android.graphics.PorterDuff
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.os.PersistableBundle
//import android.util.Log
//import android.view.Menu
//import android.view.MenuItem
//import android.view.View
//import android.widget.ProgressBar
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.widget.Toolbar
//import androidx.core.view.MenuItemCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.StaggeredGridLayoutManager
//import com.android.volley.Request
//import com.android.volley.RequestQueue
//import com.android.volley.toolbox.JsonObjectRequest
//import com.android.volley.toolbox.Volley
//import com.example.ta.Adapter.ItemAllAdapter
//import com.example.ta.Api.RetrofitAllitems
//import com.example.ta.Api.RetrofitService
//import com.example.ta.Model.MCart
//import com.example.ta.Model.Detail
//import com.example.ta.Model.MTotalCart
//import com.example.ta.Model.Url_Volley
//import com.example.ta.utilss.Tools
//import kotlinx.android.synthetic.main.action_bar_notifitcation_icon.*
//import kotlinx.android.synthetic.main.toolbar.*
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.lang.Exception
//
//class ItemAllAct : AppCompatActivity(),ItemAllAdapter.OnNoteListener {
//
//    private lateinit var ui_hot: TextView
//    lateinit var rv_all : RecyclerView
//    var list : ArrayList<Detail> = ArrayList()
//    lateinit var layoutManager : RecyclerView.LayoutManager
//    lateinit var adapter : ItemAllAdapter
//    var visibleItemCount:Int = 0
//    var pastVisibleItemCount:Int = 0
//    var totalItemCount:Int = 0
//    var loading:Boolean = false
//    var pageId:Int = 1
//    lateinit var progressBar: ProgressBar
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_item_all)
//        initToolbar()
//        progressBar = findViewById(R.id.pg_bar)
//        rv_all = findViewById(R.id.rv_allitem)
//        layoutManager = LinearLayoutManager(this)
//        rv_all.layoutManager = layoutManager
//        rv_all.layoutManager =  StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//        rv_all.setHasFixedSize(true)
//        getAllItems(pageId.toString())
//
//    }
//
//    private fun getAllItems(page: String) {
//        progressBar.visibility = View.VISIBLE
//        var apiServis:RetrofitService = RetrofitAllitems.get_client()!!.create(RetrofitService::class.java)
//        var call: Call<List<Detail>> = apiServis.getAllItem(page)
//        call.enqueue(object :Callback<List<Detail>>{
//            override fun onFailure(call: Call<List<Detail>>, t: Throwable) {
//                try {
//                    progressBar.visibility = View.GONE
//                    Log.e("tag",t!!.message)
//                }catch (e: Exception){
//
//                }
//            }
//
//            override fun onResponse(call: Call<List<Detail>>, response: Response<List<Detail>>) {
//                if(response!!.code() == 200){
//                    progressBar.visibility = View.GONE
//                    loading = true
//                    setUpAdapter(response.body())
//                }else {
//                    progressBar.visibility = View.GONE
//                }
//            }
//
//        })
//    }
//
//    private fun setUpAdapter(body: List<Detail>?) {
//        if(list.size == 0){
//            list = body as ArrayList<Detail>
//            adapter = ItemAllAdapter(this,list,this)
//
//            rv_all.adapter = adapter
//        }else {
//            var currentPosition = (rv_all!!.layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)[0]
//            list.addAll(body!!)
//            adapter.notifyDataSetChanged()
//            rv_all.scrollToPosition(currentPosition)
//        }
//        rv_all.addOnScrollListener(object : RecyclerView.OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if(dy>0){
//                    var stlm = recyclerView.layoutManager as StaggeredGridLayoutManager
//                    visibleItemCount = stlm.childCount
//                    totalItemCount = stlm.itemCount
////                    pastVisibleItemCount = (rv_all.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//
//                    pastVisibleItemCount = stlm.findFirstVisibleItemPositions(null)[0]
//                    if(loading){
//                        if((visibleItemCount+pastVisibleItemCount) >= totalItemCount){
//                            loading = false
//                            pageId++
//                            getAllItems(pageId.toString())
//                        }
//                    }
//                }
//            }
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//            }
//        })
//    }
//
//    private fun initToolbar() {
//        val toolbar = findViewById(R.id.toolbar) as Toolbar
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
//        toolbar.getNavigationIcon()?.setColorFilter(resources.getColor(R.color.indigo_500), PorterDuff.Mode.SRC_ATOP)
//        toolbar.setNavigationOnClickListener{
//            var i = Intent(this,MainActivity::class.java)
//            startActivity(i)
//        }
//        toolbar.title = "All Product"
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        Tools.setSystemBarColor(this, R.color.white_transparency)
//        Tools.setSystemBarLight(this)
//    }
//
//    override fun onNoteClick(position: Int) {
//        var intent = Intent(this, ProductDetailAct::class.java)
//        intent.putExtra("id_produk", list.get(position).id_produk.toString())
//        intent.putExtra("judul_produk", list.get(position).judul_produk)
//        intent.putExtra("harga_normal", list.get(position).harga_normal.toString())
//        intent.putExtra("foto", list.get(position).foto)
//        intent.putExtra("foto_type", list.get(position).foto_type)
//        intent.putExtra("judul_kat", list.get(position).judul_kategori)
//        intent.putExtra("judul_subkat", list.get(position).judul_subkategori)
//        intent.putExtra("stok", list.get(position).stok.toString())
//        intent.putExtra("kat_id", list.get(position).kat_id.toString())
//        intent.putExtra("subkat_id", list.get(position).subkat_id.toString())
//        intent.putExtra("berat", list.get(position).berat.toString())
//        intent.putExtra("deskripsi",list.get(position).deskripsi.toString())
//        intent.putExtra("discount",list.get(position).diskon.toString())
//        intent.putExtra("harga_diskon", list.get(position).harga_diskon.toString())
//
//        startActivity(intent)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_cart_setting_search, menu)
//        Tools.changeMenuIconColor(menu, resources.getColor(R.color.indigo_500))
//        Tools.changeOverflowMenuIconColor(toolbar, resources.getColor(R.color.indigo_500))
//        val menuItem: MenuItem = menu.findItem(R.id.aksi_cart)
//        var actionView: View = MenuItemCompat.getActionView(menuItem)
//        ui_hot = actionView.findViewById(R.id.hotlist_hot) as TextView
////        var i = Intent(this, OrderAct::class.java)
////        menuItem.intent = i
//        setupBadge()
//
//        actionView.setOnClickListener { onOptionsItemSelected(menuItem) }
//
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        if (item.itemId == android.R.id.home) {
//            finish()
//        }
//        if (item.itemId == R.id.aksi_cart)
//        {
//            var i = Intent(this, OrderAct::class.java)
//            startActivity(i)
//        }
//        if (item.itemId == R.id.action_search)
//        {
//            var i = Intent(this, ResultSearchAct::class.java)
//            startActivity(i)
//        }
//
//        return super.onOptionsItemSelected(item)
//    }
//
//    private fun setupBadge() {
//
//        if (ui_hot != null) {
//            if (MTotalCart.total_cart.toInt() == 0) {
//                if (ui_hot.getVisibility() !== View.GONE) {
//                    ui_hot.setVisibility(View.GONE)
//                }
//            } else {
////                ui_hot.setText(String.valueOf(Math.min(mCartItemCount, 99)))
//                ui_hot.setText(Math.min(MTotalCart.total_cart.toInt(),99).toString())
//                if (ui_hot.getVisibility() !== View.VISIBLE) {
//                    ui_hot.setVisibility(View.VISIBLE)
//                }
//            }
//        }
//    }
//
//    override fun onResume() {
//        getcart()
//        super.onResume()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        getcart()
//        super.onCreate(savedInstanceState, persistentState)
//    }
//
//    fun getcart()
//    {
//        var url = Url_Volley.url_website +"/udemy/get_total_cart.php?user_id="+ MCart.user_id
//        var rq: RequestQueue = Volley.newRequestQueue(this)
//        var jor = JsonObjectRequest(Request.Method.GET,url,null, com.android.volley.Response.Listener { response ->
//            //            cart_size.text = response.getInt("banyak").toString()
//            Log.e("Banyak Cart", response.getString("banyak"))
//            MTotalCart.total_cart = response.getInt("banyak")
//            MTotalCart.total_harga = response.getInt("jumlah")
//            MTotalCart.total_berat = response.getInt("berat")
//        }, com.android.volley.Response.ErrorListener { error ->
//            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
//        })
//        rq.add(jor)
//        hotlist_hot?.text = Math.min(MTotalCart.total_cart,99).toString()
//    }
//
//}
